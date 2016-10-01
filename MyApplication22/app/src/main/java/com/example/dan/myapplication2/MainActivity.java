package com.example.dan.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.util.Random;

// ***************************************
// *** NOTE:  STILL TO DO:::
//
// - add win/lose screen (and think of a way to disable everything)-- OR just create new activity
// - clean up (get rid of certain classes/ variables/ etc. that are not used/ are leftover from previous project(s))
//
// ***************************************

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView hintText;
    String hint2;
    String[] strArr;
    int[] ImageList;
    ImageView img;
    String[] hintArr;
    TextView descText;
    private static final String KEY_TEXT_VALUE = "textValue";
    private static final String KEY_TEXT_VALUE2 = "textValue2";
    private static final String IMAGE_RESOURCE = "image-resource";
    private static final String RAND_RESOURCE = "rand-resource";
    int imgNum = 0;
    SpannableString content;
    String word;
    Random randWord;
    int randWordNum = 0;
    int randWordNum2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Hang-Man");

        //Lists
        ImageList = new int[]{R.drawable.hangman0, R.drawable.hangman1, R.drawable.hangman2, R.drawable.hangman3,  R.drawable.hangman4,  R.drawable.hangman5,  R.drawable.hangman6,  R.drawable.hangman7 };
        strArr = new String[]{"apple", "cheese","cat", "grand", "money"};       //currently only works with 5-letter-lengthed words
        hintArr = new String[]{"FOOD","cows", "meow", "Awesome", "$$$"};

        //check for saved random word
        if (savedInstanceState != null) {
            randWordNum = savedInstanceState.getInt(RAND_RESOURCE, randWordNum2);
            randWordNum2 = randWordNum;
        }
        //else pick random word (and hint)
        else {
            randWord = new Random();
            int randMult = strArr.length;
            randWordNum2 = randWord.nextInt(randMult);
            randWordNum = randWordNum2;
        }

        System.out.println(randWordNum);
        word = strArr[randWordNum];   // can be set to any index of strArr
        String wordOrignal = word;    // save this for later use;

        // transform the word into the "proper" text, similar to what see on screen (easier to manipulate)
        word = word.toUpperCase();
        word = (word.replace("", " ")).trim();
        System.out.println("Word = " + word);

        // links/ references
        hintText = (TextView) findViewById(R.id.textView4);
        img = (ImageView) findViewById(R.id.imageView1);
        descText = (TextView) findViewById(R.id.textView2);

        // create "hidden" content word from the original word ("_ _ _ _")
        String content_word = (wordOrignal.replaceAll(".", "_ ")).trim();
        System.out.println("content_Word = " + content_word);
        //instead of _ _ _ _ _, we will replace this with a letter or set of letters which correspond
        // to the new string + remaining _'s, when someone clicks the "correct" button;
        // this code (right below) basically only just adds an underline underneath each letter
        content = new SpannableString(content_word);
        for (int y = 0; y < word.length(); y +=2 ){
            content.setSpan(new UnderlineSpan(), y, y+1, 0);
        }

        //set stuff
        descText.setText(content);
        img.setImageResource(ImageList[imgNum]);

        // save this anyway (no matter what orientation of phone)
        hint2 = "Hint: " + hintArr[randWordNum];
        // code for objects that do not exist in portrait vs landscape mode (in this case, hint text)
        if(hintText != null ) {
            hintText.setText(hint2);
        }

        // check for saved instance (like when flipping)  ***still not finished; must be edited!***
        if (savedInstanceState != null) {
            // get saved text
            CharSequence savedText = savedInstanceState.getCharSequence(KEY_TEXT_VALUE);
            String savedText2 = savedInstanceState.getString(KEY_TEXT_VALUE2);
            //get image position in array
            int imgNum2 = savedInstanceState.getInt(IMAGE_RESOURCE, imgNum);
            //set corresponding elements
            descText.setText(savedText);
            content = new SpannableString(savedText.toString());

            // again preventing trolls (maybe the person lost, but flips the phone very fast after losing;
            // // this will cause a null-pointer exception as well)
            int fails = ImageList.length-1;
            if (imgNum2 > fails){
                imgNum2 = fails;
            }
            img.setImageResource(ImageList[imgNum2]);

            //keep state after each rotation for imgNum (since after each rotation, imgnum gets defaulted back to zero
            // when OnCreate() is called. So we need to set it back to its "previous" value)
            imgNum = imgNum2;
        }

    }

    // save stuff (like when flipping) ***still not finished; must be edited!***
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TEXT_VALUE, descText.getText());
        outState.putString(KEY_TEXT_VALUE2, hint2);
        //outState.putCharSequence(KEY_TEXT_VALUE2, hintText.getText());
        //save image position in array
        outState.putInt(IMAGE_RESOURCE, imgNum);
        outState.putInt(RAND_RESOURCE, randWordNum2);

    }

    // the default method for onClicks (every alphabet button uses this)
    public void onClick(View v) {
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        System.out.println("here1");  //checkpoint

        //disable the button immediately to prevent trolls
        b.setEnabled(false);

        int index = word.indexOf(buttonText);
        int loop = 0;  //keeps track of how many times we looped through the word (each time we click a button)

        String content1 = "" + content;
        String content2 = "";

        while (index > -1){
        System.out.println("index = " + index);

            content2 = content1.substring(0,index) + buttonText +  content1.substring(index+1);
            System.out.println("content2 = " + content2);

            //reset content1 so that it can update on next iteration
            content1 = content2;
            index = word.indexOf(buttonText, index+1);
            loop++;
        }

        // if we did go through the loop, then change the text correctly
        if (loop > 0) {
            // retransform the word with underlines
            content = new SpannableString(content2);
            for (int y = 0; y < word.length(); y +=2 ){
                content.setSpan(new UnderlineSpan(), y, y+1, 0);
            }

            descText.setText(content);

            // add check to see if person won (man was not hung):
            // same idea as below, but with win message;

            if (content2.equals(word)){
                Toast.makeText(MainActivity.this,
                        "You Win!", Toast.LENGTH_SHORT).show();
            }

        }
        // otherwise, change the image to reflect that
        else{
            // add check to see if person lost (man is hanged):
            // my suggestion would be to "simply" change the activity to a screen that says
            // you lost (or won, but in this case, lost), and call it here;
            // the same screen may also have the new-game button
            // ***OR: might be an option to somehow pause/ disable everything
            imgNum++;
            int fails = ImageList.length-1;
            if (imgNum >= fails){            // change this to (imgArrSize-1)
                // just to prevent trolls (prevents someone clicking fast, causinng a null array pointer crash)
                if (imgNum == fails) {        // change this to (imgArrSize-1)
                    img.setImageResource(ImageList[imgNum]);
                }

                Toast.makeText(MainActivity.this,
                        "You Lose!", Toast.LENGTH_SHORT).show();
            }
            else {
                //imgNum++;
                img.setImageResource(ImageList[imgNum]);
            }

        }

    }
}
