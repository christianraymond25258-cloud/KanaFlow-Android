package com.jikana.app.model

data class KanaChar(
    val character: String,
    val romaji: String
)

data class KanaRow(
    val rowName: String,
    val characters: List<KanaChar>
)

object HiraganaData {
    val rows = listOf(
        KanaRow("A Row", listOf(
            KanaChar("あ", "a"), KanaChar("い", "i"), KanaChar("う", "u"),
            KanaChar("え", "e"), KanaChar("お", "o")
        )),
        KanaRow("Ka Row", listOf(
            KanaChar("か", "ka"), KanaChar("き", "ki"), KanaChar("く", "ku"),
            KanaChar("け", "ke"), KanaChar("こ", "ko")
        )),
        KanaRow("Sa Row", listOf(
            KanaChar("さ", "sa"), KanaChar("し", "shi"), KanaChar("す", "su"),
            KanaChar("せ", "se"), KanaChar("そ", "so")
        )),
        KanaRow("Ta Row", listOf(
            KanaChar("た", "ta"), KanaChar("ち", "chi"), KanaChar("つ", "tsu"),
            KanaChar("て", "te"), KanaChar("と", "to")
        )),
        KanaRow("Na Row", listOf(
            KanaChar("な", "na"), KanaChar("に", "ni"), KanaChar("ぬ", "nu"),
            KanaChar("ね", "ne"), KanaChar("の", "no")
        )),
        KanaRow("Ha Row", listOf(
            KanaChar("は", "ha"), KanaChar("ひ", "hi"), KanaChar("ふ", "fu"),
            KanaChar("へ", "he"), KanaChar("ほ", "ho")
        )),
        KanaRow("Ma Row", listOf(
            KanaChar("ま", "ma"), KanaChar("み", "mi"), KanaChar("む", "mu"),
            KanaChar("め", "me"), KanaChar("も", "mo")
        )),
        KanaRow("Ya Row", listOf(
            KanaChar("や", "ya"), KanaChar("ゆ", "yu"), KanaChar("よ", "yo")
        )),
        KanaRow("Ra Row", listOf(
            KanaChar("ら", "ra"), KanaChar("り", "ri"), KanaChar("る", "ru"),
            KanaChar("れ", "re"), KanaChar("ろ", "ro")
        )),
        KanaRow("Wa Row", listOf(
            KanaChar("わ", "wa"), KanaChar("を", "wo"), KanaChar("ん", "n")
        ))
    )
}

object KatakanaData {
    val rows = listOf(
        KanaRow("A Row", listOf(
            KanaChar("ア", "a"), KanaChar("イ", "i"), KanaChar("ウ", "u"),
            KanaChar("エ", "e"), KanaChar("オ", "o")
        )),
        KanaRow("Ka Row", listOf(
            KanaChar("カ", "ka"), KanaChar("キ", "ki"), KanaChar("ク", "ku"),
            KanaChar("ケ", "ke"), KanaChar("コ", "ko")
        )),
        KanaRow("Sa Row", listOf(
            KanaChar("サ", "sa"), KanaChar("シ", "shi"), KanaChar("ス", "su"),
            KanaChar("セ", "se"), KanaChar("ソ", "so")
        )),
        KanaRow("Ta Row", listOf(
            KanaChar("タ", "ta"), KanaChar("チ", "chi"), KanaChar("ツ", "tsu"),
            KanaChar("テ", "te"), KanaChar("ト", "to")
        )),
        KanaRow("Na Row", listOf(
            KanaChar("ナ", "na"), KanaChar("ニ", "ni"), KanaChar("ヌ", "nu"),
            KanaChar("ネ", "ne"), KanaChar("ノ", "no")
        )),
        KanaRow("Ha Row", listOf(
            KanaChar("ハ", "ha"), KanaChar("ヒ", "hi"), KanaChar("フ", "fu"),
            KanaChar("ヘ", "he"), KanaChar("ホ", "ho")
        )),
        KanaRow("Ma Row", listOf(
            KanaChar("マ", "ma"), KanaChar("ミ", "mi"), KanaChar("ム", "mu"),
            KanaChar("メ", "me"), KanaChar("モ", "mo")
        )),
        KanaRow("Ya Row", listOf(
            KanaChar("ヤ", "ya"), KanaChar("ユ", "yu"), KanaChar("ヨ", "yo")
        )),
        KanaRow("Ra Row", listOf(
            KanaChar("ラ", "ra"), KanaChar("リ", "ri"), KanaChar("ル", "ru"),
            KanaChar("レ", "re"), KanaChar("ロ", "ro")
        )),
        KanaRow("Wa Row", listOf(
            KanaChar("ワ", "wa"), KanaChar("ヲ", "wo"), KanaChar("ン", "n")
        ))
    )
}
