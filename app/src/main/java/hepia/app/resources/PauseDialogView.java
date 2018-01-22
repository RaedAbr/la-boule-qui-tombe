package hepia.app.resources;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import hepia.app.R;

public class PauseDialogView extends Dialog{

    public PauseDialogView(@NonNull Context context) {
        super(context);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showDialog(View.OnClickListener onClickListener){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.pause_dialog);

        Button dialogButton = findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(onClickListener);

        show();
    }
}
