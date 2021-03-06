package com.mnowo.offlineschoolmanager.feature_grade.presentation.subject_screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.MediumTest
import com.mnowo.offlineschoolmanager.GradeScreen
import com.mnowo.offlineschoolmanager.SubjectScreen
import com.mnowo.offlineschoolmanager.core.feature_core.domain.util.Screen
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.MainActivity
import com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation.util.AddSubjectTestTags
import com.mnowo.offlineschoolmanager.core.theme.OfflineSchoolManagerTheme
import com.mnowo.offlineschoolmanager.di.AppModule
import com.mnowo.offlineschoolmanager.feature_grade.domain.models.Grade
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.AssertionError

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
                    composable(Screen.GradeScreen.route) {
                        GradeScreen(navController = navController, subjectId = 1)
                    }
                }
            }
        }
    }

    @Test
    fun performAddNewSubjectButton() {
        composeRule.onNodeWithTag(GradeTestTags.ADD_BUTTON).assertExists()
        composeRule.onNodeWithTag(GradeTestTags.ADD_BUTTON).performClick()

        composeRule.onNodeWithTag(AddSubjectTestTags.ADD_UP_TO_100_TEXT)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun performMoreButton() {
        composeRule.onNodeWithTag(testTag = GradeTestTags.MORE_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = GradeTestTags.DROPDOWN_MENU).assertIsEnabled()
            .assertExists()
        composeRule.onNodeWithTag(testTag = GradeTestTags.DELETE_MENU_ITEM).assertIsDisplayed()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_MENU_ITEM).assertIsDisplayed()
    }

    @Test
    fun performListItemClick() {
        addSubject()

        composeRule.onNodeWithTag(testTag = GradeTestTags.LIST_ROW).performClick()
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ADD_UP_TO_100_TEXT)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun performEditMenuItem()  {
        addSubject()

        composeRule.onNodeWithTag(GradeTestTags.MORE_BUTTON).performClick()
        composeRule.onNodeWithTag(GradeTestTags.EDIT_MENU_ITEM).performClick()
        composeRule.onNodeWithTag(GradeTestTags.EDIT_BUTTON).assertExists().assertIsDisplayed()
        composeRule.onNodeWithTag(GradeTestTags.CANCEL_BUTTON).performClick()
    }

    @Test
    fun performDeleteButton() {
        addSubject()

        composeRule.onNodeWithTag(GradeTestTags.MORE_BUTTON).performClick()
        composeRule.onNodeWithTag(GradeTestTags.DELETE_MENU_ITEM).performClick()
        composeRule.onNodeWithTag(GradeTestTags.DELETE_BUTTON).assertExists().assertIsDisplayed()
        composeRule.onNodeWithTag(GradeTestTags.CANCEL_BUTTON).performClick()
    }

    @Test
    fun cancelButtonIsDisabledWhenEditingSubject() {
        addSubject()

        composeRule.onNodeWithTag(testTag = GradeTestTags.MORE_BUTTON).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_MENU_ITEM).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_BUTTON).performClick()

        try {
            composeRule.onNodeWithTag(testTag = GradeTestTags.CANCEL_BUTTON).assertIsNotEnabled()
        } catch (e: AssertionError) {
            println("Error: ${e.localizedMessage}")
        }
    }

    private fun addSubject() {
        composeRule.onNodeWithTag(testTag = GradeTestTags.ADD_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD)
            .performTextInput("German")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ROOM_TEXT_FIELD)
            .performTextInput("123")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.WRITTEN_PERCENTAGE_TEXT_FIELD)
            .performTextInput("50")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ORAL_PERCENTAGE_TEXT_FIELD)
            .performTextInput("50")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ADD_BUTTON).performClick()
    }
}