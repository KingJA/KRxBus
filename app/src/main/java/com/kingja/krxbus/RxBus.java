package com.kingja.krxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
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
    private Map<Class<?>, Subscription> mSubscriptionMap;
    private Map<Class<?>, Object> mStickyEventMap;
    private Subject<Object, Object> mRxBusObserverable;

    private RxBus() {
        mSubscriptionMap = new ConcurrentHashMap();
        mStickyEventMap = new ConcurrentHashMap();
        mRxBusObserverable = new SerializedSubject<>(PublishSubject.create());
    }


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
    public <T> Subscription register(Class<T> eventType, Class<?> reciver, Action1<T> action) {
        Subscription subscribe = mRxBusObserverable.ofType(eventType).subscribe(action);
        mSubscriptionMap.put(reciver, subscribe);

        return subscribe;
    }

    public <T> Observable<T> registerSticky(final Class<T> eventType, Class<?> reciver) {
        final Object event = mStickyEventMap.get(reciver);
        if (event != null) {
            return mRxBusObserverable.ofType(eventType).mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    subscriber.onNext(eventType.cast(event));
                }
            }));
        }
        return mRxBusObserverable.ofType(eventType);
    }

    public void unsubscribe(Class<?> context) {
        Subscription subscription = mSubscriptionMap.get(context);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        mStickyEventMap.remove(context);
    }

    /**
     * 发送普通事件
     *
     * @param object
     */
    public void post(Object object) {
        synchronized (RxBus.class) {
            if (mRxBusObserverable.hasObservers()) {
                mRxBusObserverable.onNext(object);
            }
        }

    }


    /**
     * 发送延迟事件
     *
     * @param object
     */
    public void postSticky(Object object, Class<?> receiver) {
        synchronized (RxBus.class) {
            mStickyEventMap.put(receiver, object);
        }
        post(object);
    }


}
