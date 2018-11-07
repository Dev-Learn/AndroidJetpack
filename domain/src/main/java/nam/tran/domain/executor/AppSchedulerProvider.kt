///*
// *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *      https://mindorks.com/license/apache-v2
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License
// */
//
//package nam.tran.domain.executor
//
//import javax.inject.Inject
//
//import io.reactivex.Scheduler
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
//class AppSchedulerProvider @Inject
//constructor() : SchedulerProvider {
//
//    override fun ui(): Scheduler {
//        return AndroidSchedulers.mainThread()
//    }
//
//    override fun computation(): Scheduler {
//        return Schedulers.computation()
//    }
//
//    override fun io(): Scheduler {
//        return Schedulers.io()
//    }
//
//}
