package gr.greekbusses.misc;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

public class EmptyTokenizer extends MultiAutoCompleteTextView.CommaTokenizer
{
    private int i;

    @Override
    public int findTokenStart(CharSequence inputText, int cursor)
    {
        int idx = cursor;

        while (idx > 0 && inputText.charAt(idx - 1) != ' ')
        {
            idx--;
        }
        while (idx < cursor && inputText.charAt(idx) == ' ')
        {
            idx++;
        }
        return idx;
    }
    @Override
    public int findTokenEnd(CharSequence inputText, int cursor)
    {
        int idx = cursor;
        int length = inputText.length();

        while (idx < length)
        {
            if (inputText.charAt(i) == ' ')
            {
                return idx;
            }
            else
            {
                idx++;
            }
        }
        return length;
    }
    @Override
    public CharSequence terminateToken(CharSequence inputText)
    {
        int idx = inputText.length();

        while (idx > 0 && inputText.charAt(idx - 1) == ' ')
        {
            idx--;
        }
        if (idx > 0 && inputText.charAt(idx - 1) == ' ')
        {
            return inputText;
        }
        else
        {
            if (inputText instanceof Spanned)
            {
                SpannableString sp = new SpannableString(inputText + " ");
                TextUtils.copySpansFrom((Spanned) inputText, 0, inputText.length(), Object.class, sp, 0);
                return sp;
            }
            else
            {
                return inputText;
            }
        }
    }
}