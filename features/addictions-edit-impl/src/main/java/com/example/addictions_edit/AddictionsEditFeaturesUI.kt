package com.example.addictions_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.misufoil.addictions.theme.AddictionTheme
import dev.misufoil.addictions.uikit.R

@ExperimentalMaterial3Api
@Preview
@Composable
fun AddictionsEditScreen() {
    val text by remember { mutableStateOf("Alcohol") }
    // вынести в viewmodel

    Surface(tonalElevation = 5.dp, modifier = Modifier.background(AddictionTheme.colorScheme.surfaceVariant)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {

            Text(
                text = "Зависимость",
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                style = AddictionTheme.typography.titleLarge

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))

            ) {
                Text(
                    modifier = Modifier.padding(16.dp).weight(1f),
                    text = text,
                    style = AddictionTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = ImageVector
                        .vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = "Floating action button.",
                    modifier = Modifier.padding(16.dp).size(24.dp)
                )

            }
        }
    }

}
//OutlinedTextField(
//
//                modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
//                value = text,
//                onValueChange = { text = it },
//                label = { Text("Label  пппппппппппппп") }
//            )
//
//@Composable
//fun AddictionsEditScreen() {
////    AddictionsMainScreen(viewModel = viewModel())
//}

//@Composable
//fun AddictionsMainScreen() {
//    AddictionsMainScreen(viewModel = viewModel())
//}

//@Composable
//fun AddictionsEditScreen() {
//    AddictionsEditScreen()
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun AddictionsEditScreen() {
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val scope = rememberCoroutineScope()
//    var showBottomSheet by remember { mutableStateOf(false) }
//    val addictionList = AddictionTypes.entries
//    Surface {
////        Button( onClick = { showBottomSheet = true } ) {
////            Text("Show bottom sheet")
////        }
//        Box {
//            Text()
//        }
//    }
//
//    Scaffold(
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                text = {  },
//                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//                onClick = {
//                    showBottomSheet = true
//                },
//            )
//        }
//    ) { contentPadding ->
//        // Screen content
//
//        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
//            if (showBottomSheet) {
//                BottomSheet(showBottomSheet, sheetState, addictionList)
//            }
//        }
//    }
//}
//
//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//private fun BottomSheet(
//    showBottomSheet: Boolean,
//    sheetState: SheetState,
//    addictionList: EnumEntries<AddictionTypes>
//) {
//
//    var showBottomSheet1 = showBottomSheet
//    ModalBottomSheet(
//        modifier = Modifier.fillMaxSize(),
//        onDismissRequest = {
//            showBottomSheet1 = false
//        },
//        sheetState = sheetState,
//    ) {
//        Card(
//            modifier = Modifier.padding(10.dp).background(color = Color.LightGray),
//            shape = RoundedCornerShape(8.dp),
//            elevation = CardDefaults.cardElevation(
//                defaultElevation = 5.dp
//            ),
//
//        ) {
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                items(addictionList) { addiction ->
//                    Addiction(addiction)
//                }
//            }
//        }
//
//    }
//}
//
//
//@Composable
//internal fun Addiction(
//    addiction: AddictionTypes
//) {
//    Row {
//        Text(
//            text = addiction.description,
//            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
//            fontSize = 24.sp
//        )
//    }
//
//}