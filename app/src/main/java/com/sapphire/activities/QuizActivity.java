package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.adapters.AnswersAdapter;
import com.sapphire.api.GetQuizAction;
import com.sapphire.logic.QuestionData;
import com.sapphire.logic.QuizData;

public class QuizActivity extends AppCompatActivity implements AnswersAdapter.OnRootClickListener,
                                                               GetQuizAction.RequestQuiz,
                                                               GetQuizAction.RequestQuizData{
    private String quizeId;
    ProgressDialog pd;
    private QuizData quizData;
    private int currentQuestion = 0;
    private TextView questionName;
    private ListView answerlist;
    private AnswersAdapter adapter;
    private TextView text_header;
    private Button button_previous;
    private Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pd.show();
                //needClose = true;
                //new PolicyLogAction(CoursActivity.this, id, Environment.PolicyStatusAcknowledged).execute();
                finish();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        text_header = (TextView) findViewById(R.id.text_header);
        questionName = (TextView) findViewById(R.id.questionName);

        Intent intent = getIntent();
        text_header.setText(intent.getStringExtra("name"));

        quizeId = intent.getStringExtra("quizeId");

        answerlist = (ListView) findViewById(R.id.answerlist);
        adapter = new AnswersAdapter(this);
        answerlist.setAdapter(adapter);

        button_previous = (Button) findViewById(R.id.button_previous);
        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion = currentQuestion - 1;
                UpdateQuiz();
            }
        });

        button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion == -2) {
                    finish();
                } else if (quizData.getQuestions().size()-1 == currentQuestion) {
                    currentQuestion = -2;
                } else {
                    currentQuestion = currentQuestion + 1;
                }
                UpdateQuiz();
            }
        });

        pd.show();
        new GetQuizAction(QuizActivity.this, quizeId).execute();
    }

    @Override
    public void onRequestQuiz(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateQuiz() {
        if (quizData == null || quizData.getQuestions().size() <= currentQuestion) {
            return;
        }
        text_header.setText(quizData.getName());
        if (currentQuestion == -1) {
            questionName.setText(getResources().getString(R.string.text_quiz_will_take) + " 30 " + getResources().getString(R.string.text_to_complete));
        } else if (currentQuestion == -2) {
            questionName.setText(getResources().getString(R.string.text_your_quiz_score) + " 36" + "!");
        } else {
            QuestionData questionData = quizData.getQuestions().get(currentQuestion);
            questionName.setText(questionData.getName());
            adapter.setListArray(questionData.getAnswers());
        }

        UpdateButtons();
    }

    public void UpdateButtons() {
        if (quizData == null || quizData.getQuestions().size() <= currentQuestion) {
            return;
        }
        if (currentQuestion == -1) {
            button_next.setText(getResources().getString(R.string.text_start));
        } else if (currentQuestion == -2) {
            button_next.setText(getResources().getString(R.string.text_back_courses));
            answerlist.setVisibility(View.GONE);
        } else if (quizData.getQuestions().size()-1 == currentQuestion) {
            button_next.setText(getResources().getString(R.string.text_finish));
        } else {
            button_next.setText(getResources().getString(R.string.text_next));
        }
        if (currentQuestion <= 0) {
            button_previous.setVisibility(View.GONE);
        } else {
            button_previous.setVisibility(View.VISIBLE);
        }

        boolean isChecked = false;
        if (currentQuestion >= 0) {
            QuestionData questionData = quizData.getQuestions().get(currentQuestion);
            for (int i = 0; i < questionData.getAnswers().size(); i++) {
                if (questionData.getAnswers().get(i).getChecked()) {
                    isChecked = true;
                    break;
                }
            }
        } else {
            isChecked = true;
        }

        button_next.setEnabled(isChecked);
    }

    @Override
    public void onRequestQuizData(QuizData quizData) {
        this.quizData = quizData;

        currentQuestion = -1;
        UpdateQuiz();

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {
        UpdateButtons();
    }

    @Override
    public void onBackPressed() {
        if (currentQuestion > 0) {
            currentQuestion = currentQuestion - 1;
            UpdateQuiz();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
