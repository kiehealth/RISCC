package com.cronelab.riscc.support

import android.app.Application
import com.cronelab.riscc.support.analytics.AppAnalyticsRepository
import com.cronelab.riscc.support.analytics.AppAnalyticsViewModelFactory
import com.cronelab.riscc.support.data.ApiFactory
import com.cronelab.riscc.support.data.database.configuration.AppDatabase
import com.cronelab.riscc.ui.aboutUs.AboutUsRepository
import com.cronelab.riscc.ui.aboutUs.AboutUsViewModelFactory
import com.cronelab.riscc.ui.answer.model.AnswerRepository
import com.cronelab.riscc.ui.answer.model.AnswerViewModelFactory
import com.cronelab.riscc.ui.auth.login.viewModel.UserViewModelFactory
import com.cronelab.riscc.ui.auth.login.viewModel.UserRepository
import com.cronelab.riscc.ui.dashboard.viewModel.AppVersionFactory
import com.cronelab.riscc.ui.dashboard.viewModel.AppVersionRepository
import com.cronelab.riscc.ui.feedback.model.FeedbackRepository
import com.cronelab.riscc.ui.feedback.model.FeedbackViewModelFactory
import com.cronelab.riscc.ui.links.model.LinksRepository
import com.cronelab.riscc.ui.links.model.LinksViewModelFactory
import com.cronelab.riscc.ui.note.model.NoteRepository
import com.cronelab.riscc.ui.note.model.NoteViewModelFactory
import com.cronelab.riscc.ui.notification.model.NotificationRepository
import com.cronelab.riscc.ui.notification.model.NotificationVMFactory
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireRepository
import com.cronelab.riscc.ui.questionnaire.model.QuestionnaireViewModelFactory
import com.cronelab.riscc.ui.resources.ResourceRepository
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppController : Application(), KodeinAware {
    companion object {
        lateinit var instance: AppController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppController))

//        bind() from singleton { ApiFactory(instance()) }
        bind() from singleton { ApiFactory() }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from provider { UserViewModelFactory(instance()) }

        bind() from singleton { LinksRepository(instance(), instance()) }
        bind() from provider { LinksViewModelFactory(instance()) }

        bind() from singleton { NotificationRepository(instance(), instance()) }
        bind() from provider { NotificationVMFactory(instance()) }

        bind() from singleton { FeedbackRepository(instance(), instance()) }
        bind() from provider { FeedbackViewModelFactory(instance()) }

        bind() from singleton { NoteRepository(instance(), instance()) }
        bind() from provider { NoteViewModelFactory(instance()) }

        bind() from singleton { QuestionnaireRepository(instance(), instance()) }
        bind() from provider { QuestionnaireViewModelFactory(instance()) }

        bind() from singleton { AnswerRepository(instance(), instance()) }
        bind() from provider { AnswerViewModelFactory(instance()) }

        bind() from singleton { AppVersionRepository(instance(), instance()) }
        bind() from provider { AppVersionFactory(instance()) }

        bind() from singleton { AboutUsRepository(instance(), instance()) }
        bind() from provider { AboutUsViewModelFactory(instance()) }

        bind() from singleton { ResourceRepository(instance(), instance()) }
        bind() from provider { ResourceViewModelFactory(instance()) }

        bind() from singleton { AppAnalyticsRepository(instance(), instance()) }
        bind() from provider { AppAnalyticsViewModelFactory(instance()) }
    }
}