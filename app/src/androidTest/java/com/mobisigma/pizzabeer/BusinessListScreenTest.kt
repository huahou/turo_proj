package com.mobisigma.pizzabeer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mobisigma.pizzabeer.domain.model.BusinessEntity
import com.mobisigma.pizzabeer.ui.view.screens.BusinessRowUI
import org.junit.Rule
import org.junit.Test

class BusinessListScreenTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun businessListScreen_titleDisplayed() {
        composeTestRule.setContent {
            BusinessRowUI(
                index = 0,
                businessEntity = BusinessEntity(
                    id = "-AOrlLEK9jt6LUzvUD1vQg",
                    name = "First Class Pizza",
                    imageUrl = "",
                    rating = 4.5,
                    reviewCount = 223
            )){

            }
        }

        composeTestRule
            .onNodeWithText("First Class Pizza")
            .assertIsDisplayed()
    }
}