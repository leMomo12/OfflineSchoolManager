package com.mnowo.offlineschoolmanager.core.feature_subject.add_subject.presentation

import androidx.compose.ui.res.stringResource
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
import com.mnowo.offlineschoolmanager.feature_grade.presentation.util.GradeTestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
@MediumTest
class AddSubjectBottomSheetTest {

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
    fun addTextWhenAddingSubject() {
        composeRule.onNodeWithTag(testTag = GradeTestTags.ADD_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ADD_BUTTON).assertTextEquals("Add")
    }

    @Test
    fun saveTextWhenEditingSubject() {
        addSubject()

        composeRule.onNodeWithTag(testTag = GradeTestTags.MORE_BUTTON).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_MENU_ITEM).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ADD_BUTTON).assertTextEquals("Save")
    }

    @Test
    fun textFieldsHaveInformationWhenEditSubject() {
        addSubject()

        composeRule.onNodeWithTag(testTag = GradeTestTags.MORE_BUTTON).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_MENU_ITEM).performClick()
        composeRule.onNodeWithTag(testTag = GradeTestTags.EDIT_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD)
            .assert(
                hasText("German")
            )
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ROOM_TEXT_FIELD)
            .assertTextContains("123")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.WRITTEN_PERCENTAGE_TEXT_FIELD)
            .assert(
                hasText("50.0")
            )
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ORAL_PERCENTAGE_TEXT_FIELD)
            .assert(hasText("50.0"))

    }

    @Test
    fun inputText() {
        composeRule.onNodeWithTag(testTag = GradeTestTags.ADD_BUTTON).performClick()

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD)
            .performTextInput("German")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD).assertValueEquals("German")

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ROOM_TEXT_FIELD)
            .performTextInput("Abcd")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD).assert(hasText("Abcd"))

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.WRITTEN_PERCENTAGE_TEXT_FIELD)
            .performTextInput("50")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD).assert(hasText("50"))

        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.ORAL_PERCENTAGE_TEXT_FIELD)
            .performTextInput("50")
        composeRule.onNodeWithTag(testTag = AddSubjectTestTags.SUBJECT_TEXT_FIELD).assert(hasText("50"))
    }

    @Test
    fun pickColor() {

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