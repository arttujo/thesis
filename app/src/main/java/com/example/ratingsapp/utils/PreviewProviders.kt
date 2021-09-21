package com.example.ratingsapp.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.Review

class ReviewProvider : PreviewParameterProvider<Review> {
    override val values: Sequence<Review> = sequenceOf(
        Review(1, 1, 1, "Test", "Maija", "TEST", 3, 5000),
        Review(1, 1, 1, "Test", "Matti", "TEST", 5, 213),
        Review(
            1,
            1,
            1,
            "Test",
            "Very extremely long username that is most likely not likely",
            "TEST",
            5,
            213
        )

    )
}

class ReviewListProvider : PreviewParameterProvider<List<Review>> {
    override val values: Sequence<List<Review>> = sequenceOf(
        listOf(
            Review(1, 1, 1, "Test", "Maija", "TEST", 3, 5000),
            Review(1, 1, 1, "Test", "Matti", "TEST", 5, 213),
            Review(
                1,
                1,
                1,
                "Test",
                "Very extremely long username that is most likely not likely",
                "TEST",
                5,
                213
            ),
            Review(1, 1, 1, "Test", "Maija", "TEST", 3, 5000),
            Review(1, 1, 1, "Test", "Matti", "TEST", 5, 213),
            Review(
                1,
                1,
                1,
                "Test",
                "Very extremely long username that is most likely not likely",
                "TEST",
                5,
                213
            )
        )
    )
}


class GameListProvider : PreviewParameterProvider<List<Game>> {
    override val values: Sequence<List<Game>> = sequenceOf(
        listOf(
            Game(
                1,
                "Assasin's Creed",
                "Ubisoft",
                "http://foxerserver.asuscomm.com/images/covers/ac.png",
                0
            ),
            Game(
                2,
                "Age of Empires II",
                "Microsoft",
                "http://foxerserver.asuscomm.com/images/covers/aoe.png",
                0
            ),
            Game(
                3,
                "Conan Exiles",
                "Funcom",
                "http://foxerserver.asuscomm.com/images/covers/ce.png",
                0
            ),
            Game(
                4,
                "Counter Strike: Global Offensive",
                "Valve",
                "http://foxerserver.asuscomm.com/images/covers/cs.png",
                0
            ),
            Game(
                5,
                "Deus Ex: Mankind Divided",
                "Square Enix",
                "http://foxerserver.asuscomm.com/images/covers/de.png",
                0
            ),
            Game(
                6,
                "Fallout 3",
                "Bethesda",
                "http://foxerserver.asuscomm.com/images/covers/f.jpg",
                0
            ),
            Game(
                7,
                "Forza Horizon 4",
                "Microsoft",
                "http://foxerserver.asuscomm.com/images/covers/fh4.png",
                0
            ),
            Game(
                8,
                "Halo: The Master Cheif Collection",
                "Microsoft",
                "http://foxerserver.asuscomm.com/images/covers/halo.png",
                0
            ),
            Game(
                9,
                "Half-Life 2",
                "Valve",
                "http://foxerserver.asuscomm.com/images/covers/hl.png",
                0
            ),
            Game(
                10,
                "Mafia",
                "Square Enix",
                "http://foxerserver.asuscomm.com/images/covers/mafia.png",
                0
            ),
            Game(
                11,
                "Mario Kart 64",
                "Nintendo",
                "http://foxerserver.asuscomm.com/images/covers/mk.png",
                0
            ),
            Game(
                12,
                "Team Fortress 2",
                "Valve",
                "http://foxerserver.asuscomm.com/images/covers/tf.png",
                0
            ),
            Game(
                13,
                "Warammer: Vermintide II",
                "Fatshark",
                "http://foxerserver.asuscomm.com/images/covers/wv.png",
                0
            )
        )

    )
}
