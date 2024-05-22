package com.jakala.reordertest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jakala.reordertest.ui.theme.ReorderTestTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReorderTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state = rememberLazyListState()
                    var data by remember { mutableStateOf(List(100) { "Item $it" }) }

                    LazyColumn(
                        state = state,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        itemsIndexed(data, key = { _, item -> item }) { index, item ->
                            var allowAnimation by remember {
                                mutableStateOf(true)
                            }
                            val context = LocalContext.current
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (allowAnimation) {
                                            Modifier.animateItemPlacement()
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .combinedClickable(
                                        onLongClick = {
                                            allowAnimation = !allowAnimation
                                            Toast
                                                .makeText(
                                                    context,
                                                    "$item animations: $allowAnimation",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },
                                        onClick = {
                                            data = data
                                                .toMutableList()
                                                .apply {
                                                    val movedItem = this[index]
                                                    removeAt(index)
                                                    add((index - 1).coerceAtLeast(0), movedItem)
                                                }
                                        }
                                    )
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .padding(8.dp)
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

