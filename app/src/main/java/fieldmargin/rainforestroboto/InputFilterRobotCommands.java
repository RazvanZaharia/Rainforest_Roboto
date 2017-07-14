package fieldmargin.rainforestroboto;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterRobotCommands implements InputFilter {

    private final String COMMANDS = "NSWEPD";

    public InputFilterRobotCommands() {
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int lastCharPos = source.length() - 1;
        Character ch = source.charAt(lastCharPos);
        if (COMMANDS.indexOf(ch) >= 0) {
            return null;
        } else {
            source = source.subSequence(0, lastCharPos);
        }
        return source;
    }
}