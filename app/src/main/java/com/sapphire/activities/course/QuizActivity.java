package com.sapphire.activities.course;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.AnswersAdapter;
import com.sapphire.api.GetQuizAction;
import com.sapphire.api.PostQuizzesAction;
import com.sapphire.api.QuizzesLogAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.QuestionData;
import com.sapphire.models.QuizData;
import com.sapphire.models.QuizScoreData;

public class QuizActivity extends BaseActivity implements AnswersAdapter.OnRootClickListener,
                                                          GetQuizAction.RequestQuiz,
                                                          GetQuizAction.RequestQuizData,
                                                          PostQuizzesAction.RequestPostQuizzes,
                                                          PostQuizzesAction.RequestPostQuizzesData,
                                                          QuizzesLogAction.RequestQuizzesLog,
                                                          UpdateAction.RequestUpdate{
    private String quizeId = "";
    private ProgressDialog pd;
    private QuizData quizData;
    private int currentQuestion = 0;
    private TextView questionName;
    private ListView answerlist;
    private AnswersAdapter adapter;
    private TextView text_header;
    private Button button_previous;
    private Button button_next;
    private String duration = "";
    private String quizScore = "";
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private boolean isPassed = false;

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
        duration = intent.getStringExtra("duration");

        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        try {
            hours = Integer.parseInt(duration.substring(0, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            minutes = Integer.parseInt(duration.substring(3, 5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            seconds = Integer.parseInt(duration.substring(6, 8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        duration = "";
        if (hours > 0) {
            duration = hours + " " + getResources().getString(R.string.hours);
        }
        if (minutes > 0) {
            if (!duration.equals("")) {
                duration = duration + " ";
            }
            duration = minutes + " " + getResources().getString(R.string.minutes);
        }
        if (seconds > 0) {
            if (!duration.equals("")) {
                duration = duration + " ";
            }
            duration = seconds + " " + getResources().getString(R.string.seconds);
        }

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
                    pd.show();
                    new PostQuizzesAction(QuizActivity.this, quizeId, quizData).execute();
                } else if (currentQuestion == -1) {
                    pd.show();
                    new QuizzesLogAction(QuizActivity.this, quizeId, Environment.AccountQuizStatusStarted).execute();
                } else {
                    currentQuestion = currentQuestion + 1;
                    UpdateQuiz();
                }
            }
        });

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        pd.show();
        new GetQuizAction(QuizActivity.this, quizeId).execute();

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(QuizActivity.this);
            }
        });

        UpdateBottom();
    }

    private void UpdateBottom() {
        if (Sapphire.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    @Override
    public void onRequestQuiz(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    public void UpdateQuiz() {
        if (quizData == null || quizData.getQuestions().size() <= currentQuestion) {
            return;
        }
        text_header.setText(quizData.getName());
        if (currentQuestion == -1) {
            //questionName.setText(getResources().getString(R.string.text_quiz_will_take) + " " + duration + " " + getResources().getString(R.string.text_to_complete));
            questionName.setText(getResources().getString(R.string.text_quiz_test));
        } else if (currentQuestion == -2) {
            if (isPassed) {
                questionName.setText(getResources().getString(R.string.text_your_quiz_score) + " " + quizScore + "!");
            } else {
                questionName.setText(getResources().getString(R.string.text_quiz_failed));
            }
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
        if (isChecked) {
            button_next.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            button_next.setTextColor(ContextCompat.getColor(this, R.color.grey));
        }
    }

    @Override
    public void onRequestQuizData(QuizData quizData) {
        this.quizData = quizData;
        quizScore = "";

        currentQuestion = -1;
        UpdateQuiz();

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {
        UpdateButtons();
    }

    @Override
    public void onRequestPostQuizzes(String result) {
        pd.hide();
        Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        if (result.equals(getResources().getString(R.string.text_unauthorized))) {
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
            Intent intExit = new Intent(Environment.BROADCAST_ACTION);
            try {
                intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                Sapphire.getInstance().sendBroadcast(intExit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    @Override
    public void onRequestPostQuizzesData(QuizScoreData quizScoreData) {
        quizScore = quizScoreData.getScore();
        isPassed = quizScoreData.getIsPassed();
        currentQuestion = -2;
        UpdateQuiz();

        pd.hide();
    }

    @Override
    public void onRequestQuizzesLog(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            currentQuestion = currentQuestion + 1;
            UpdateQuiz();

            pd.hide();
        }
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
        unregisterReceiver(br);
    }
}
