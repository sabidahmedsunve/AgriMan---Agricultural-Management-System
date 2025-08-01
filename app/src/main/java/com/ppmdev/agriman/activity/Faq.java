package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ppmdev.agriman.R;
import com.ppmdev.agriman.model.FaqModel;

import java.util.ArrayList;
import java.util.List;

public class Faq extends AppCompatActivity {

    private LinearLayout faqContainer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq);


        init();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Faq.this,HomePage.class));
            }
        });



        List<FaqModel> faqList = new ArrayList<>();
        faqList.add(new FaqModel("How do I change the profile picture?", "On the profile page, click on the profile picture and upload the new one."));
        faqList.add(new FaqModel("How do I find the latest weather update?", "On the Home Page top, you can get the latest weather update."));
        faqList.add(new FaqModel("How do I create new advertisement?", "On the Home Page, you can find the Market-place, go to the Market-place and then top right corner you can find the new advertisement creation option."));
        faqList.add(new FaqModel("How do I create new blog?", "On the Blogs page bottom right corner you can find the new blog creation option."));
        faqList.add(new FaqModel("How do I get my data?", "On the Profile page you can get your user data."));
        faqList.add(new FaqModel("How do I log out form this app?", "AOn the bottom of the Profile page you can find the log out option."));
        faqList.add(new FaqModel("How do I know the Market price of any product?", "Go to the Market-place page and you can find all advertisement are available at this moment."));

        for (FaqModel faqItem : faqList) {
            addFaqItem(faqItem);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void addFaqItem(FaqModel faqItem) {
        View faqView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.card_faq, faqContainer, false);

        TextView questionTextView = faqView.findViewById(R.id.tv_question);
        TextView answerTextView = faqView.findViewById(R.id.tv_answer);

        questionTextView.setText("Question: "+faqItem.getQuestion());
        answerTextView.setText("Answer: "+faqItem.getAnswer());

        faqContainer.addView(faqView);
    }
    private void init(){
        faqContainer = findViewById(R.id.faq_container);
        toolbar=findViewById(R.id.toolbar_faq);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}


