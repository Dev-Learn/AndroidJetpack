package tran.nam.core;

import javax.inject.Inject;

import tran.nam.core.view.BaseActivity;

@SuppressWarnings("unused")
abstract public class Navigator {

    public void finish(BaseActivity activity) {
        activity.finish();
        activity.overridePendingTransition(tran.nam.core.R.anim.slide_in_left, tran.nam.core.R.anim.slide_out_right);
    }

    protected void animationTransition(BaseActivity activity) {
        activity.overridePendingTransition(tran.nam.core.R.anim.slide_in_right, tran.nam.core.R.anim.slide_out_left);
    }

    public void exit(BaseActivity activity) {
        activity.finish();
    }
}
