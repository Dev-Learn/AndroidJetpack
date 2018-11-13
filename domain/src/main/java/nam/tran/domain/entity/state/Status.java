package nam.tran.domain.entity.state;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static nam.tran.domain.entity.state.Status.ERROR;
import static nam.tran.domain.entity.state.Status.LOADING;
import static nam.tran.domain.entity.state.Status.SUCCESS;

/**
 * Status of a resource that is provided to the UI.
 * <p>
 * These are usually created by the Repository classes where they return
 * {@code LiveData<Resource<T>>} to pass back the latest data to the UI with its fetch status.
 */

@IntDef({LOADING, SUCCESS, ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface Status {
    int LOADING = 1;
    int SUCCESS = 2;
    int ERROR = 3;
}
