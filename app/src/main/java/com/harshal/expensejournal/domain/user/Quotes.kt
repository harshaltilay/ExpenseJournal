/*
 *
 *  Copyright (C) 2023 Harshal Tilay
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


package com.harshal.expensejournal.domain.user

import kotlin.random.Random

class Quotes {
    companion object {
        private val listQuotes = arrayListOf(
            "Don't tell me where your priorities are. Show me where you spend your money and I'll tell you what they are",
            "Remember, saving money is a journey, not a destination, so start small and keep at it!",
            "The art of saving wise is the art of knowing what to overlook.",
            "Saving is a very important part of getting rich.",
            "Wealth is not his that has it, but his that enjoys it.",
            "A rupee saved is a rupee earned",
            "The habit of saving is itself an education; it fosters every virtue.",
            "Saving teaches self-denial, cultivates the sense of order, trains to forethought, and so broadens the mind.",
            "Congratulations on taking control of your finances and making saving a priority!",
            "Your hard work and dedication will pay off in the long run.",
            "Well done on making smart decisions with your money.",
            "Your commitment to saving will bring you closer to your financial goals and bring peace of mind.",
            "Saving money is not always easy, but it's a habit that pays off.",
            "Congratulations on your discipline and perseverance. Your future self will thank you.",
            "A penny saved is a penny earned, and you're well on your way to a bright financial future",
            "Congratulations on your wise choices.",
            "Saving money is a sign of maturity and responsibility.",
            "Congratulations on making it a priority in your life and taking control of your financial future.",
            "It takes courage and discipline to save money and make smart financial decisions.",
            "the key to stopping overspending is to be mindful and intentional about your spending habits",
            "It takes time and practice, but with persistence, you can develop healthier spending habits",
            "Make a conscious effort to change overspending habits.",
            "Make a budget, Stick to it",
            "Avoid impulse purchases: Impulse purchases are often driven by emotions, such as boredom, stress, or excitement.",
            "To avoid making these purchases, give yourself time to think before making a purchase. ",
            "Ask yourself if you really need the item and if it fits into your budget.",
            "Find alternative ways to have fun: Instead of spending money on entertainment",
            "Find alternative activities that don't cost anything or very little"
        )

        fun getNormalQuote(): String {
            return listQuotes[Random.Default.nextInt(listQuotes.size)]
        }
    }
}