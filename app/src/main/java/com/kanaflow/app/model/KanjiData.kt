package com.kanaflow.app.model

data class KanjiCard(
    val kanji: String,
    val meaning: String,
    val onyomi: String,
    val kunyomi: String,
    val example: String = "",
    val level: Int = 1
)

data class SRSCard(
    val kanjiCard: KanjiCard,
    val interval: Int = 1,
    val easeFactor: Float = 2.5f,
    val correctCount: Int = 0,
    val wrongCount: Int = 0
)

object KanjiData {
    val level1 = listOf(
        KanjiCard("日", "Sun / Day", "ニチ、ジツ", "ひ、か", "日本 (にほん) Japan", 1),
        KanjiCard("月", "Moon / Month", "ゲツ、ガツ", "つき", "月曜日 (げつようび) Monday", 1),
        KanjiCard("火", "Fire", "カ", "ひ", "火曜日 (かようび) Tuesday", 1),
        KanjiCard("水", "Water", "スイ", "みず", "水曜日 (すいようび) Wednesday", 1),
        KanjiCard("木", "Tree / Wood", "モク、ボク", "き", "木曜日 (もくようび) Thursday", 1),
        KanjiCard("金", "Gold / Money", "キン、コン", "かね", "金曜日 (きんようび) Friday", 1),
        KanjiCard("土", "Earth / Soil", "ド、ト", "つち", "土曜日 (どようび) Saturday", 1),
        KanjiCard("山", "Mountain", "サン", "やま", "富士山 (ふじさん) Mt. Fuji", 1),
        KanjiCard("川", "River", "セン", "かわ", "川口 (かわぐち) Kawaguchi", 1),
        KanjiCard("人", "Person", "ジン、ニン", "ひと", "人口 (じんこう) Population", 1)
    )

    val level2 = listOf(
        KanjiCard("大", "Big / Large", "ダイ、タイ", "おお", "大学 (だいがく) University", 2),
        KanjiCard("小", "Small", "ショウ", "ちい、こ", "小学校 (しょうがっこう) Elementary school", 2),
        KanjiCard("中", "Middle / Inside", "チュウ", "なか", "中学校 (ちゅうがっこう) Middle school", 2),
        KanjiCard("上", "Up / Above", "ジョウ、ショウ", "うえ、あ", "上手 (じょうず) Skillful", 2),
        KanjiCard("下", "Down / Below", "カ、ゲ", "した、さ", "下手 (へた) Unskillful", 2),
        KanjiCard("左", "Left", "サ", "ひだり", "左手 (ひだりて) Left hand", 2),
        KanjiCard("右", "Right", "ウ、ユウ", "みぎ", "右手 (みぎて) Right hand", 2),
        KanjiCard("口", "Mouth", "コウ、ク", "くち", "入口 (いりぐち) Entrance", 2),
        KanjiCard("手", "Hand", "シュ", "て", "手紙 (てがみ) Letter", 2),
        KanjiCard("目", "Eye", "モク、ボク", "め", "目標 (もくひょう) Goal", 2)
    )

    val level3 = listOf(
        KanjiCard("食", "Eat / Food", "ショク", "た、く", "食事 (しょくじ) Meal", 3),
        KanjiCard("飲", "Drink", "イン", "の", "飲み物 (のみもの) Beverage", 3),
        KanjiCard("見", "See / Look", "ケン", "み", "見学 (けんがく) Field trip", 3),
        KanjiCard("聞", "Hear / Listen", "ブン、モン", "き", "新聞 (しんぶん) Newspaper", 3),
        KanjiCard("言", "Say / Speak", "ゲン、ゴン", "い、こと", "言語 (げんご) Language", 3),
        KanjiCard("書", "Write", "ショ", "か", "書道 (しょどう) Calligraphy", 3),
        KanjiCard("読", "Read", "ドク、トク", "よ", "読書 (どくしょ) Reading", 3),
        KanjiCard("来", "Come", "ライ", "く、き", "来週 (らいしゅう) Next week", 3),
        KanjiCard("行", "Go", "コウ、ギョウ", "い、おこな", "旅行 (りょこう) Travel", 3),
        KanjiCard("帰", "Return", "キ", "かえ", "帰国 (きこく) Return home", 3)
    )

    val level4 = listOf(
        KanjiCard("学", "Study / Learn", "ガク", "まな", "学生 (がくせい) Student", 4),
        KanjiCard("校", "School", "コウ", "", "高校 (こうこう) High school", 4),
        KanjiCard("先", "Previous / Before", "セン", "さき", "先生 (せんせい) Teacher", 4),
        KanjiCard("生", "Life / Birth", "セイ、ショウ", "い、う、なま", "誕生日 (たんじょうび) Birthday", 4),
        KanjiCard("年", "Year", "ネン", "とし", "今年 (ことし) This year", 4),
        KanjiCard("時", "Time / Hour", "ジ", "とき", "時間 (じかん) Time", 4),
        KanjiCard("間", "Between / Space", "カン、ケン", "あいだ、ま", "時間 (じかん) Time", 4),
        KanjiCard("今", "Now / Present", "コン、キン", "いま", "今日 (きょう) Today", 4),
        KanjiCard("何", "What", "カ", "なに、なん", "何時 (なんじ) What time", 4),
        KanjiCard("円", "Yen / Circle", "エン", "まる", "百円 (ひゃくえん) 100 yen", 4)
    )

    val level5 = listOf(
        KanjiCard("東", "East", "トウ", "ひがし", "東京 (とうきょう) Tokyo", 5),
        KanjiCard("西", "West", "セイ、サイ", "にし", "関西 (かんさい) Kansai", 5),
        KanjiCard("南", "South", "ナン、ナ", "みなみ", "南口 (みなみぐち) South exit", 5),
        KanjiCard("北", "North", "ホク", "きた", "北海道 (ほっかいどう) Hokkaido", 5),
        KanjiCard("国", "Country", "コク", "くに", "外国 (がいこく) Foreign country", 5),
        KanjiCard("語", "Language", "ゴ", "かた", "日本語 (にほんご) Japanese", 5),
        KanjiCard("英", "England / English", "エイ", "", "英語 (えいご) English", 5),
        KanjiCard("本", "Book / Origin", "ホン", "もと", "日本 (にほん) Japan", 5),
        KanjiCard("字", "Character / Letter", "ジ", "あざ", "漢字 (かんじ) Kanji", 5),
        KanjiCard("文", "Sentence / Writing", "ブン、モン", "ふみ", "文化 (ぶんか) Culture", 5)
    )

    val allLevels = listOf(
        "Level 1 — Nature & Days" to level1,
        "Level 2 — Directions & Body" to level2,
        "Level 3 — Actions & Verbs" to level3,
        "Level 4 — School & Time" to level4,
        "Level 5 — Places & Language" to level5
    )
}