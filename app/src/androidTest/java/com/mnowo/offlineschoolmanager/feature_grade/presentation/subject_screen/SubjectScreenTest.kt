package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.MediumTest
import com.mnowo.offlineschoolmanager.SubjectScreen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.MainActivity
import com.mnowo.offlineschoolmanager.core.theme.OfflineSchoolManagerTheme
import com.mnowo.offlineschoolmanager.di.AppModule
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
@MediumTest
class SubjectScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            OfflineSchoolManagerTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.SubjectScreen.route
                ) {
                    composable(Screen.SubjectScreen.route) {
                        SubjectScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun performAddNewSubjectButton() {
        composeRule.onNodeWithTag(GradeTestTags.ADD_BUTTON).assertExists()
        composeRule.onNodeWithTag(GradeTestTags.ADD_BUTTON).performClick()
    }

    @Test
    fun performMoreButton() {
        composeRule.onNodeWithTag(testTag = GradeTestTags.MORE_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = GradeTestTags.DROPDOWN_MENU).assertIsEnabled()
            .assertExists()
    }

    @Test
    fun performListItemClick() {

    }

    @Test
    fun performEditButton() {

    }

    @Test
    fun performDeleteButton() {

    }
}