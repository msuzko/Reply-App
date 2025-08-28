package com.mec.reply.test

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mec.reply.R
import com.mec.reply.data.local.LocalEmailsDataProvider
import com.mec.reply.ui.ReplyApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

private const val time: Long = 0

class ReplyAppStateRestorationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_selectedEmailEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Compact)
        }
        val email2Body = getString(LocalEmailsDataProvider.allEmails[2].body)
        composeTestRule.onNodeWithText(email2Body).assertIsDisplayed()
        pause()
        composeTestRule.onNodeWithText(email2Body).performClick()
        composeTestRule.onNodeWithText(email2Body).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertExists()
        pause()
        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()
        composeTestRule.onNodeWithText(email2Body).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertExists()
        pause()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_selectedEmailEmailRetained_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Expanded)
        }
        val email2Body = getString(LocalEmailsDataProvider.allEmails[2].body)
        composeTestRule.onNodeWithText(email2Body).assertIsDisplayed()
        pause()
        composeTestRule.onNodeWithText(email2Body).performClick()
        composeTestRule.onNodeWithTagForStringId(R.string.details_screen).onChildren()
            .assertAny(hasAnyDescendant(hasText(email2Body)))
        pause()
        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()
        composeTestRule.onNodeWithTagForStringId(R.string.details_screen).onChildren()
            .assertAny(hasAnyDescendant(hasText(email2Body)))
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertDoesNotExist()

        pause()

    }

    private fun pause() {
        runBlocking {

            delay(time)
        }
    }

    private fun getString(id: Int): String = composeTestRule.activity.getString(id)

}