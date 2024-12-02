package com.znhst.xtzb.ui.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.znhst.xtzb.R
import com.znhst.xtzb.viewModel.FreezerDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FreezerDetail(deviceNo: String, viewModel: FreezerDetailViewModel = viewModel()) {
    val historyList by viewModel.historyList.collectAsState()
    val openDoorImgId = R.drawable.door_open
    val closeDoorImgId = R.drawable.door_close

    // 数据加载
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.fetchHistory(deviceNo)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(historyList) { index, item ->
            // 用 Card 包裹每条记录
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 门状态图标
                    Image(
                        painter = painterResource(
                            id = if (item.doorStatus == "开门") openDoorImgId else closeDoorImgId
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp), // 图标增大
                        colorFilter = ColorFilter.tint(
                            if (item.doorStatus == "开门") Color(0xFF4CAF50) // 绿色表示开门
                            else Color(0xFFF44336) // 红色表示关门
                        )
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // 图标与文本分隔

                    Column(
                        modifier = Modifier.weight(1f) // 分布式布局
                    ) {
                        // 操作时间
                        Text(
                            text = "操作时间: ${item.time}",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(8.dp))
                        // 动作描述
                        Text(
                            text = "动作: ${item.doorStatus}",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 列表分隔线
            if (index < historyList.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}