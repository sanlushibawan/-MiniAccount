package com.sanlushibawan.miniaccount.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.sanlushibawan.miniaccount.R
import com.sanlushibawan.miniaccount.entity.BillEntity
import com.sanlushibawan.miniaccount.page.viewModels.BillListViewModel
import com.sanlushibawan.miniaccount.ui.theme.MiniAccountTheme

class BillListActivity : ComponentActivity() {
    private lateinit var billListViewModel: BillListViewModel
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniAccountTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    billListViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(BillListViewModel::class.java)
                    var showDetailDialogFlag by remember {
                        billListViewModel.showDetailDialogFlag
                    }
                    val billList = remember {
                        billListViewModel.billList
                    }
                    Scaffold (
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = { Text(text = "所有账单") }, navigationIcon = {
                                    IconButton(onClick = { this.finish() }) {
                                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                                    }
                                }

                            )
                        }
                    ){ innerPadding->
                        var showDetailBill by remember {
                            mutableStateOf(
                            if (billList.isEmpty())BillEntity(1999,1,1,"一",true,0,"",0.0f,"")
                            else billList[0]) }
                        LazyColumn(contentPadding = innerPadding, modifier = Modifier.padding(horizontal = 16.dp)) {
                            val yearPanel = billList.distinctBy { it.yearStr }
                            items(yearPanel.size){yearIndex->
                                Text(text = ("${yearPanel[yearIndex].yearStr}年"))
                                val yearList = billList.filter { it.yearStr == yearPanel[yearIndex].yearStr }
                                val monthPanel = yearList.distinctBy { it.monthStr }
                                repeat(monthPanel.size){ monthIndex->
                                    Column {//年份
                                        val monthBillList = yearList.filter { it.monthStr == monthPanel[monthIndex].monthStr }
                                        val dayPanel = monthBillList.distinctBy { it.dayStr }
                                        Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                                            Text(text = "${monthPanel[monthIndex].monthStr} 月")
                                            repeat(dayPanel.size){ dayIndex ->
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    shadowElevation = 3.dp,
                                                    modifier = Modifier
                                                        .fillMaxWidth()) {
                                                    Row (Modifier.padding(8.dp)){
                                                        //每月支出信息
                                                        Column {
                                                            Text(text = dayPanel[dayIndex].dayStr.toString(), style = MaterialTheme.typography.bodyLarge)
                                                            Text(text = "星期${dayPanel[dayIndex].weekStr}", style = MaterialTheme.typography.bodySmall)
                                                        }
                                                        Column {
                                                            val dayDisplayList = monthBillList.filter {it.dayStr == dayPanel[dayIndex].dayStr }
                                                            repeat(dayDisplayList.size){it->
                                                                Column {
                                                                    //每日支出信息
                                                                    Row (modifier = Modifier.clickable {
                                                                        showDetailBill = dayDisplayList[it]
                                                                        showDetailDialogFlag = !showDetailDialogFlag}){
                                                                        //每条支出信息
                                                                        if(dayDisplayList[it].billType)
                                                                            Icon(painter = painterResource(
                                                                                id = R.drawable.expenses_one
                                                                            ), contentDescription = "支出箭头", tint = MaterialTheme.colorScheme.onBackground)
                                                                        else
                                                                            Icon(painter = painterResource(
                                                                                id = R.drawable.income_one
                                                                            ), contentDescription = "收入箭头", tint = MaterialTheme.colorScheme.error)
                                                                        Column {
                                                                            Text(text = if(dayDisplayList[it].billType) "支出" else "收入")
                                                                            Text(text = dayDisplayList[it].payAccountName)
                                                                        }
                                                                        Spacer(modifier = Modifier.width(50.dp))
                                                                        Row (Modifier.fillMaxWidth()){
                                                                            Column {
                                                                                val showMoney = if(dayDisplayList[it].billType) "-${dayDisplayList[it].billAmount}" else dayDisplayList[it].billAmount.toString()
                                                                                Text(text = showMoney, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                                                                                Text(text = dayDisplayList[it].remarks,Modifier.fillMaxWidth(), textAlign = TextAlign.End, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                                            }
                                                                        }
                                                                    }
                                                                    if(it!=dayDisplayList.size-1)Divider()
                                                                }
                                                            }
                                                        }
                                                    }


                                                }
                                            }
                                            //每月总结
                                            var out_pay = 0f
                                            var in_come = 0f
                                            monthBillList.forEach{
                                                if (it.billType) out_pay += it.billAmount
                                                else in_come += it.billAmount
                                            }
                                            Card{
                                                Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(text = "当月总结")
                                                    Divider()
                                                    Row {
                                                        Text(text = "支出：￥-$out_pay", color = MaterialTheme.colorScheme.onBackground)
                                                        Text(text = "收入：￥$in_come", modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.error, textAlign = TextAlign.End)
                                                    }
                                                    Text(text = "总结：￥${in_come-out_pay}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                                                }
                                            }
                                        }

                                    }
                                }

                            }
                        }
                        if(showDetailDialogFlag){
                            Dialog(onDismissRequest = { showDetailDialogFlag = !showDetailDialogFlag },) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column (Modifier.padding(bottom = 16.dp)){
                                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)){
                                            Row (modifier = Modifier
                                                .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                IconButton(onClick = { /*TODO 删除数据*/ }) {
                                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "删除这条数据", tint = MaterialTheme.colorScheme.error)
                                                }
                                                Text(modifier = Modifier
                                                    .weight(1.0f),
                                                    text = "${showDetailBill.yearStr}年${showDetailBill.monthStr}月${showDetailBill.dayStr}日交易详情",
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.titleMedium)
                                                IconButton(onClick = { showDetailDialogFlag = !showDetailDialogFlag }) {
                                                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "关闭窗口")
                                                }
                                            }
                                        }
                                        Divider()
                                        Column (
                                            Modifier
                                                .weight(1.0f)
                                                .padding(16.dp, 8.dp),Arrangement.spacedBy(8.dp)){
                                            Row {
                                                Text(text = "交易类型：")
                                                Text(text = if(showDetailBill.billType)"支出" else "收入")
                                            }
                                            Row {
                                                Text(text = "交易账户：")
                                                Text(text = showDetailBill.payAccountName)
                                            }
                                            Row {
                                                Text(text = "交易数额：")
                                                Text(text = showDetailBill.billAmount.toString())
                                            }
                                            Row {
                                                Text(text = "备注：")
                                                Text(text = showDetailBill.remarks)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}