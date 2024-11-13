package dev.misufoil.core_utils.models

import android.content.Context


enum class AddictionTypes(val descriptionResId: Int) {
    ALCOHOL(dev.misufoil.addictions.uikit.R.string.alcohol),
    VAPING(dev.misufoil.addictions.uikit.R.string.vaping),
    JUNK_FOOD(dev.misufoil.addictions.uikit.R.string.junk_food),
    CAFFEINE(dev.misufoil.addictions.uikit.R.string.caffeine),
    MASTURBATION(dev.misufoil.addictions.uikit.R.string.masturbation),
    DRUGS(dev.misufoil.addictions.uikit.R.string.drugs),
    NICOTINE(dev.misufoil.addictions.uikit.R.string.nicotine),
    GAMBLING(dev.misufoil.addictions.uikit.R.string.gambling),
    INTERNET(dev.misufoil.addictions.uikit.R.string.internet),
    PORNOGRAPHY(dev.misufoil.addictions.uikit.R.string.pornography),
    SEX(dev.misufoil.addictions.uikit.R.string.sex),
    CIGARETTES(dev.misufoil.addictions.uikit.R.string.cigarettes),
    SUGAR(dev.misufoil.addictions.uikit.R.string.sugar),
    TOBACCO(dev.misufoil.addictions.uikit.R.string.tobacco),
    FOOD(dev.misufoil.addictions.uikit.R.string.food),
    SHOPPING(dev.misufoil.addictions.uikit.R.string.shopping),
    WORK(dev.misufoil.addictions.uikit.R.string.work),
    VIDEO_GAMES(dev.misufoil.addictions.uikit.R.string.video_games),
    ENERGY_DRINKS(dev.misufoil.addictions.uikit.R.string.energy_drinks);

    companion object {
        fun fromDescription(context: Context, description: String): AddictionTypes? {
            return entries.find {
                context.getString(it.descriptionResId).equals(description, ignoreCase = true)
            }
        }

        fun getDescription(context: Context, type: AddictionTypes): String {
            return context.getString(type.descriptionResId)
        }
    }
}
