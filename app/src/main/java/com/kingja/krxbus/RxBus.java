package com.kingja.krxbus;

import rx.Subscription;
import rx.functions.Action1;
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
     *
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
     * 注册一个观察者
     *
     * @param eventType 要接收的事件类型
     * @param action    要处理的回调
     * @param <T>
     * @return
     */
    public <T> Subscription register(Class<T> eventType, Action1<T> action) {
        return mRxBusObserverable.ofType(eventType).subscribe(action);
    }

    /**
     * 发送
     *
     * @param object
     */
    public void post(Object object) {
        if (mRxBusObserverable.hasObservers()) {
            mRxBusObserverable.onNext(object);
        }
    }


}
