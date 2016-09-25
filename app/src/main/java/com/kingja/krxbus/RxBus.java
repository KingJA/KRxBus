package com.kingja.krxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Description：TODO
 * Create Time：2016/9/25 14:28
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class RxBus {
    private volatile static RxBus mRxBus;

    private RxBus() {
    }

    private Subject<Object, Object> mRxBusObserverable = new SerializedSubject<>(PublishSubject.create());

    /**
     * 获得单例
     * @return
     */
    public static RxBus getDefault() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;

    }

    /**
     * 获得一个被观察者
     * @param eventType 事件的类型，进行筛选用
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mRxBusObserverable.ofType(eventType);
    }

    /**
     * 发送
     * @param object
     */
    public void post(Object object) {
        if (hasObservers()) {
            mRxBusObserverable.onNext(object);
        }
    }

    /**
     * 判断是否有观察者
     * @return
     */
    public boolean hasObservers() {
        return mRxBusObserverable.hasObservers();
    }

}
