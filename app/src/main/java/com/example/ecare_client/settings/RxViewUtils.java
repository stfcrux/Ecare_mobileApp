package com.example.ecare_client.settings;


import android.support.design.widget.TextInputLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Func1;

/**
 * Created by marcel on 31/07/15.
 */
public class RxViewUtils {

    public static Observable<Boolean> verifyInput(TextInputLayout input,
                                                  Func1<? super String, Boolean> action) {
        return WidgetObservable.text(input.getEditText(), true).compose(apply(action));
    }

    public static Func1<String, Boolean> checkPhone() {
        // TODO implement
        return s -> s.length() > 4;
    }

    public static Func1<String, Boolean> checkText(int minLength) {
        return s -> s.length() > minLength;
    }

    public static Func1<String, Boolean> checkEmail() {
        return s -> s.length() > 4 && s.contains("@") && s.contains(".");
    }

    private static Observable.Transformer<? super OnTextChangeEvent, Boolean> apply(
            Func1<? super String, Boolean> action) {
        return observable -> observable.debounce(600, TimeUnit.MILLISECONDS)
                .map(event -> event.text().toString())
                .map(action)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread());
    }
//    public void addInfo(TextInputLayout inputEmail, TextInputLayout inputPhone, TextInputLayout inputName) {
//
//        List<Observable<Boolean>> observableList = new ArrayList<>(4);
//
//        observableList.add(verifyInput(inputEmail, checkEmail()).doOnNext(
//                isValid -> inputEmail.setError(isValid ? "" : "Invalid email")));
//
//        observableList.add(verifyInput(inputPhone, checkPhone()).doOnNext(
//                isValid -> inputPhone.setError(isValid ? "" : "Invalid phone")));
//
//        observableList.add(verifyInput(inputName, checkText(2)).doOnNext(
//                isValid -> inputName.setError(isValid ? "" : "Not a real name")));
//
//
//        addSubscription(Observable.combineLatest(observableList, args -> {
//            for (Object o : args) {
//                if (!(Boolean) o) {
//                    return false;
//                }
//            }
//            return true;
//        }).subscribe(view::setEnableSubmit));
//    }
}
