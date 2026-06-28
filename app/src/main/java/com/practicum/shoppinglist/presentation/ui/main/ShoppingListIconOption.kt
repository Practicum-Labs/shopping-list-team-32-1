package com.practicum.shoppinglist.presentation.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.BabyChangingStation
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.BrunchDining
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.CardTravel
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ChildFriendly
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DesktopMac
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DinnerDining
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.EmojiNature
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Hiking
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Icecream
import androidx.compose.material.icons.outlined.Iron
import androidx.compose.material.icons.outlined.KingBed
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.LaptopMac
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocalPizza
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.OutdoorGrill
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Plumbing
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TabletMac
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.Toys
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.outlined.Weekend
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.ui.graphics.vector.ImageVector

data class ShoppingListIconOption(
    val name: String,
    val icon: ImageVector,
)

val ShoppingListIconOptions = listOf(
    ShoppingListIconOption(name = "shopping_cart", icon = Icons.Outlined.ShoppingCart),
    ShoppingListIconOption(name = "shopping_bag", icon = Icons.Outlined.ShoppingBag),
    ShoppingListIconOption(name = "grocery_store", icon = Icons.Outlined.LocalGroceryStore),
    ShoppingListIconOption(name = "restaurant", icon = Icons.Outlined.Restaurant),
    ShoppingListIconOption(name = "local_dining", icon = Icons.Outlined.LocalDining),
    ShoppingListIconOption(name = "cafe", icon = Icons.Outlined.LocalCafe),
    ShoppingListIconOption(name = "pizza", icon = Icons.Outlined.LocalPizza),
    ShoppingListIconOption(name = "fastfood", icon = Icons.Outlined.Fastfood),
    ShoppingListIconOption(name = "bakery", icon = Icons.Outlined.BakeryDining),
    ShoppingListIconOption(name = "egg", icon = Icons.Outlined.Egg),
    ShoppingListIconOption(name = "icecream", icon = Icons.Outlined.Icecream),
    ShoppingListIconOption(name = "bar", icon = Icons.Outlined.LocalBar),
    ShoppingListIconOption(name = "liquor", icon = Icons.Outlined.Liquor),
    ShoppingListIconOption(name = "kitchen", icon = Icons.Outlined.Kitchen),
    ShoppingListIconOption(name = "ramen", icon = Icons.Outlined.RamenDining),
    ShoppingListIconOption(name = "lunch", icon = Icons.Outlined.LunchDining),
    ShoppingListIconOption(name = "dinner", icon = Icons.Outlined.DinnerDining),
    ShoppingListIconOption(name = "brunch", icon = Icons.Outlined.BrunchDining),
    ShoppingListIconOption(name = "seafood", icon = Icons.Outlined.SetMeal),
    ShoppingListIconOption(name = "grill", icon = Icons.Outlined.OutdoorGrill),
    ShoppingListIconOption(name = "flowers", icon = Icons.Outlined.LocalFlorist),
    ShoppingListIconOption(name = "yard", icon = Icons.Outlined.Yard),
    ShoppingListIconOption(name = "nature", icon = Icons.Outlined.EmojiNature),
    ShoppingListIconOption(name = "tools", icon = Icons.Outlined.Build),
    ShoppingListIconOption(name = "construction", icon = Icons.Outlined.Construction),
    ShoppingListIconOption(name = "hardware", icon = Icons.Outlined.Hardware),
    ShoppingListIconOption(name = "paint", icon = Icons.Outlined.FormatPaint),
    ShoppingListIconOption(name = "plumbing", icon = Icons.Outlined.Plumbing),
    ShoppingListIconOption(name = "electric", icon = Icons.Outlined.ElectricalServices),
    ShoppingListIconOption(name = "cleaning", icon = Icons.Outlined.CleaningServices),
    ShoppingListIconOption(name = "laundry", icon = Icons.Outlined.LocalLaundryService),
    ShoppingListIconOption(name = "dry_cleaning", icon = Icons.Outlined.DryCleaning),
    ShoppingListIconOption(name = "iron", icon = Icons.Outlined.Iron),
    ShoppingListIconOption(name = "clothes", icon = Icons.Outlined.Checkroom),
    ShoppingListIconOption(name = "bedroom", icon = Icons.Outlined.KingBed),
    ShoppingListIconOption(name = "chair", icon = Icons.Outlined.Chair),
    ShoppingListIconOption(name = "weekend", icon = Icons.Outlined.Weekend),
    ShoppingListIconOption(name = "lightbulb", icon = Icons.Outlined.Lightbulb),
    ShoppingListIconOption(name = "celebration", icon = Icons.Outlined.Celebration),
    ShoppingListIconOption(name = "cake", icon = Icons.Outlined.Cake),
    ShoppingListIconOption(name = "gift", icon = Icons.Outlined.CardGiftcard),
    ShoppingListIconOption(name = "medical", icon = Icons.Outlined.MedicalServices),
    ShoppingListIconOption(name = "hospital", icon = Icons.Outlined.LocalHospital),
    ShoppingListIconOption(name = "medication", icon = Icons.Outlined.Medication),
    ShoppingListIconOption(name = "vaccines", icon = Icons.Outlined.Vaccines),
    ShoppingListIconOption(name = "health", icon = Icons.Outlined.HealthAndSafety),
    ShoppingListIconOption(name = "spa", icon = Icons.Outlined.Spa),
    ShoppingListIconOption(name = "volunteer", icon = Icons.Outlined.VolunteerActivism),
    ShoppingListIconOption(name = "pets", icon = Icons.Outlined.Pets),
    ShoppingListIconOption(name = "store", icon = Icons.Outlined.Storefront),
    ShoppingListIconOption(name = "mall", icon = Icons.Outlined.LocalMall),
    ShoppingListIconOption(name = "offer", icon = Icons.Outlined.LocalOffer),
    ShoppingListIconOption(name = "school", icon = Icons.Outlined.School),
    ShoppingListIconOption(name = "run", icon = Icons.AutoMirrored.Outlined.DirectionsRun),
    ShoppingListIconOption(name = "fitness", icon = Icons.Outlined.FitnessCenter),
    ShoppingListIconOption(name = "soccer", icon = Icons.Outlined.SportsSoccer),
    ShoppingListIconOption(name = "basketball", icon = Icons.Outlined.SportsBasketball),
    ShoppingListIconOption(name = "tennis", icon = Icons.Outlined.SportsTennis),
    ShoppingListIconOption(name = "volleyball", icon = Icons.Outlined.SportsVolleyball),
    ShoppingListIconOption(name = "hiking", icon = Icons.Outlined.Hiking),
    ShoppingListIconOption(name = "pool", icon = Icons.Outlined.Pool),
    ShoppingListIconOption(name = "beach", icon = Icons.Outlined.BeachAccess),
    ShoppingListIconOption(name = "home", icon = Icons.Outlined.Home),
    ShoppingListIconOption(name = "sports", icon = Icons.Outlined.SportsEsports),
    ShoppingListIconOption(name = "videogame", icon = Icons.Outlined.VideogameAsset),
    ShoppingListIconOption(name = "toys", icon = Icons.Outlined.Toys),
    ShoppingListIconOption(name = "mood", icon = Icons.Outlined.SelfImprovement),
    ShoppingListIconOption(name = "palette", icon = Icons.Outlined.Palette),
    ShoppingListIconOption(name = "brush", icon = Icons.Outlined.Brush),
    ShoppingListIconOption(name = "color_lens", icon = Icons.Outlined.ColorLens),
    ShoppingListIconOption(name = "movie", icon = Icons.Outlined.Movie),
    ShoppingListIconOption(name = "theater", icon = Icons.Outlined.TheaterComedy),
    ShoppingListIconOption(name = "music", icon = Icons.Outlined.MusicNote),
    ShoppingListIconOption(name = "headphones", icon = Icons.Outlined.Headphones),
    ShoppingListIconOption(name = "car", icon = Icons.Outlined.DirectionsCar),
    ShoppingListIconOption(name = "bike", icon = Icons.AutoMirrored.Outlined.DirectionsBike),
    ShoppingListIconOption(name = "train", icon = Icons.Outlined.Train),
    ShoppingListIconOption(name = "flight", icon = Icons.Outlined.Flight),
    ShoppingListIconOption(name = "trash", icon = Icons.Outlined.DeleteOutline),
    ShoppingListIconOption(name = "camera", icon = Icons.Outlined.CameraAlt),
    ShoppingListIconOption(name = "photo_camera", icon = Icons.Outlined.PhotoCamera),
    ShoppingListIconOption(name = "photo_library", icon = Icons.Outlined.PhotoLibrary),
    ShoppingListIconOption(name = "phone", icon = Icons.Outlined.PhoneIphone),
    ShoppingListIconOption(name = "laptop", icon = Icons.Outlined.LaptopMac),
    ShoppingListIconOption(name = "desktop", icon = Icons.Outlined.DesktopMac),
    ShoppingListIconOption(name = "tablet", icon = Icons.Outlined.TabletMac),
    ShoppingListIconOption(name = "watch", icon = Icons.Outlined.Watch),
    ShoppingListIconOption(name = "devices", icon = Icons.Outlined.Devices),
    ShoppingListIconOption(name = "book", icon = Icons.AutoMirrored.Outlined.MenuBook),
    ShoppingListIconOption(name = "stroller", icon = Icons.Outlined.ChildFriendly),
    ShoppingListIconOption(name = "baby", icon = Icons.Outlined.BabyChangingStation),
    ShoppingListIconOption(name = "puzzle", icon = Icons.Outlined.Extension),
    ShoppingListIconOption(name = "work", icon = Icons.Outlined.WorkOutline),
    ShoppingListIconOption(name = "travel_bag", icon = Icons.Outlined.CardTravel),
    ShoppingListIconOption(name = "wallet", icon = Icons.Outlined.AccountBalanceWallet),
    ShoppingListIconOption(name = "money", icon = Icons.Outlined.AttachMoney),
    ShoppingListIconOption(name = "credit_card", icon = Icons.Outlined.CreditCard),
    ShoppingListIconOption(name = "savings", icon = Icons.Outlined.Savings),
    ShoppingListIconOption(name = "receipt", icon = Icons.AutoMirrored.Outlined.ReceiptLong),
    ShoppingListIconOption(name = "star", icon = Icons.Outlined.StarBorder),
    ShoppingListIconOption(name = "list_alt", icon = Icons.AutoMirrored.Outlined.ListAlt),
)

fun shoppingListIconByName(iconName: String): ImageVector {
    return ShoppingListIconOptions
        .firstOrNull { iconOption -> iconOption.name == iconName }
        ?.icon
        ?: Icons.AutoMirrored.Outlined.ListAlt
}
