package com.ibcorp.helpmeapp.Fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentQuizBinding
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.model.quiz.Result
import com.ibcorp.helpmeapp.presentation.di.Injector
import com.ibcorp.helpmeapp.presentation.news.NewsViewModel
import com.ibcorp.helpmeapp.presentation.news.NewsViewModelFactory
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    @Inject
    lateinit var factory: NewsViewModelFactory
    private var param1: String? = null
    private var param2: String? = null
    private var questionCounter = 0
    private var questionCountTotal = 0
    private var score = 0
    private lateinit var newsViewModel: NewsViewModel
    private var currentQuestion: Result? = null
    private var answered = false
    private lateinit var questionList: List<Result>
    private var correctAnswer = 0
    var selectedRadioButton = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        val view: View = binding.getRoot()
        (requireActivity().application as Injector).createNewsSubComponent().inject(this)
        newsViewModel = ViewModelProvider(this, factory)
            .get(NewsViewModel::class.java)
        Utils.captureFirebaseActionEvents("Quiz", "Quiz Fragment", requireContext())
        displayNewsSource(binding)
        return view
    }

    private fun displayNewsSource(binding: FragmentQuizBinding) {
        binding.buttonStartQuiz.setOnClickListener {
            binding.startQuiz.visibility = View.GONE
            binding.quizProgressBar.visibility = View.VISIBLE
            val responseLiveData = newsViewModel.getQuiz()
            responseLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.questionaire.visibility = View.VISIBLE
                    binding.quizProgressBar.visibility = View.GONE
                    Log.i("TAG", "DATA:" + it)
                    questionList = it
                    questionCountTotal = it.size
                    showNextQuestion(binding)
                } else {
                    binding.quizProgressBar.visibility = View.GONE
                    Toast.makeText(activity, "No data available", Toast.LENGTH_LONG).show()
                }
            })
        }
        binding.buttonConfirmNext.setOnClickListener {
            if (!answered) {
                if (binding.radioButton1.isChecked || binding.radioButton2.isChecked) {
                    checkAnswer(binding)
                } else {
                    CustomToast.snackbar("Please select an answer", binding.questionaire)
                }
            } else {
                showNextQuestion(binding)
            }
        }

    }

    private fun checkAnswer(binding: FragmentQuizBinding) {
        answered = true
        val rbSelected: RadioButton =
            binding.radioGroup.findViewById(binding.radioGroup.getCheckedRadioButtonId())
//        val answerNr: Int = binding.radioGroup.indexOfChild(rbSelected)

        if (!selectedRadioButton.isNullOrBlank()&&currentQuestion!!.correctAnswer.equals(selectedRadioButton)&&currentQuestion!!.correctAnswer.equals("True")) {
            score = score + 15
            binding.textViewScore.text = "Earn Points: $score"
            binding.radioButton1.setTextColor(Color.GREEN)
            binding.radioButton2.setTextColor(Color.RED)
            binding.textViewQuestion.setText("Answer is correct")
//            correctAnswer = 0
        }else if(!selectedRadioButton.isNullOrBlank()&&currentQuestion!!.correctAnswer.equals(selectedRadioButton)&&currentQuestion!!.correctAnswer.equals("False")){
            score = score + 15
            binding.radioButton1.setTextColor(Color.RED)
            binding.radioButton2.setTextColor(Color.GREEN)
            binding.textViewQuestion.setText("Answer is correct")
        }else if(!selectedRadioButton.isNullOrBlank()&&!currentQuestion!!.correctAnswer.equals(selectedRadioButton)&&currentQuestion!!.correctAnswer.equals("False")){
//            correctAnswer = 1
            binding.radioButton1.setTextColor(Color.RED)
            binding.radioButton2.setTextColor(Color.GREEN)
            binding.textViewQuestion.setText("Answer is Incorrect")
        }else if(!selectedRadioButton.isNullOrBlank()&&!currentQuestion!!.correctAnswer.equals(selectedRadioButton)&&currentQuestion!!.correctAnswer.equals("True")){
//            correctAnswer = 1
            binding.radioButton1.setTextColor(Color.GREEN)
            binding.radioButton2.setTextColor(Color.RED)
            binding.textViewQuestion.setText("Answer is Incorrect")
        }
        showSolution(binding)
    }

    private fun showSolution(binding: FragmentQuizBinding) {
//        binding.radioButton1.setTextColor(Color.RED)
//        binding.radioButton2.setTextColor(Color.RED)
//        when (correctAnswer) {
//            0 -> {
//                binding.radioButton1.setTextColor(Color.GREEN)
//                binding.textViewQuestion.setText("Answer is correct")
//            }
//            1 -> {
//                binding.radioButton2.setTextColor(Color.GREEN)
//                binding.radioButton1.setTextColor(Color.RED)
//                binding.textViewQuestion.setText("Answer is Incorrect")
//            }
//        }
        if (questionCounter < questionCountTotal) {
            binding.buttonConfirmNext.setText("Next")
        } else {
            questionCounter = 0
            binding.textViewHighscore.text = "Your Earn points: $score"
            binding.buttonConfirmNext.setText("Finish")
            binding.startQuiz.visibility = View.VISIBLE
            binding.questionaire.visibility = View.GONE
        }
    }


    private fun showNextQuestion(binding: FragmentQuizBinding) {
        binding.radioButton1.setTextColor(Color.BLACK)
        binding.radioButton2.setTextColor(Color.BLACK)
        binding.radioGroup.clearCheck()
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var rbButton = group.findViewById<RadioButton>(checkedId)
            if(rbButton!=null)
            selectedRadioButton = rbButton.text.toString()
        }
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter)
            var question = currentQuestion!!.question
            if (question == null) {
                // return an empty spannable if the html is null
                SpannableString("")
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
                // we are using this flag to give a consistent behaviour
                Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY);
            } else {
                Html.fromHtml(question);
            }
            var htmlAsSpanned = Html.fromHtml(currentQuestion!!.question)
            binding.textViewQuestion.setText(htmlAsSpanned)
//            binding.radioButton1.setText(currentQuestion!!.correctAnswer)
//            binding.radioButton2.setText(currentQuestion!!.incorrectAnswers.get(0))
            questionCounter++
            binding.textViewQuestionCount.setText("Question: $questionCounter/$questionCountTotal")
            answered = false
            binding.buttonConfirmNext.setText("Confirm")
        } else {
            requireActivity()!!.finish();
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}