/*
 * Copyright 2017, Wolfhard Fehre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicefontaine.seanachie.data;


import com.nicefontaine.seanachie.BuildConfig;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusException;
import de.greenrobot.event.NoSubscriberEvent;
import de.greenrobot.event.SubscriberExceptionEvent;
import timber.log.Timber;


public final class DefaultEventBus {

    private static final Object LOCK = new Object();
    private static DefaultEventBus instance;

    public static DefaultEventBus getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new DefaultEventBus();
                }
            }
        }
        return instance;
    }

    private DefaultEventBus() {}

    public void register(final Object subscriber) {
        try {
            if (!EventBus.getDefault().isRegistered(subscriber)) {
                EventBus.getDefault().register(subscriber);
                Timber.d("Register '%s'", subscriber.getClass().getSimpleName());
            }
        } catch (final EventBusException exception) {
            Timber.d(exception.getMessage());
        }
    }

    public void unregister(final Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
            Timber.d("Unregister %s", subscriber.getClass().getSimpleName());
        }
    }

    public void subscriberException(final SubscriberExceptionEvent event) {
        if (BuildConfig.DEBUG) {
            Timber.e("'%s' in event '%s' in class '%s' >>> %s",
                    event.causingSubscriber.getClass().getSimpleName(),
                    event.causingEvent.getClass().getCanonicalName(),
                    event.throwable.toString(),
                    event.throwable);
        }
    }

    public void noSubscriberException(final NoSubscriberEvent event) {
        if (BuildConfig.DEBUG) {
            Timber.e("No subscriber for event '%s'",
                    event.originalEvent.getClass().getCanonicalName());
        }
    }

    public EventBus getEventBusImplementation() {
        return EventBus.getDefault();
    }

    public <T> T getStickyEventAndRemoveIt(final Class<T> eventType) {
        final T stickyEvent = getEventBusImplementation().getStickyEvent(eventType);
        if (stickyEvent != null) {
            getEventBusImplementation().removeStickyEvent(stickyEvent);
        }

        return stickyEvent;
    }

    public void post(final Object event) {
        EventBus.getDefault().post(event);
    }

    public void postSticky(final Object event) {
        EventBus.getDefault().postSticky(event);
    }
}
