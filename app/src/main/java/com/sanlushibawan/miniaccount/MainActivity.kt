package com.sanlushibawan.miniaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.entity.BillEntity
import com.sanlushibawan.miniaccount.page.AccountListActivity
import com.sanlushibawan.miniaccount.page.BillListActivity
import com.sanlushibawan.miniaccount.page.OpenFirstTimeActivity
import com.sanlushibawan.miniaccount.page.viewModels.MainViewModel
import com.sanlushibawan.miniaccount.ui.theme.MiniAccountTheme
import java.util.Calendar
import kotlin.math.log

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val openFirstTime=getSharedPreferences("MiniAccount", Context.MODE_PRIVATE).getBoolean("isFirstTime",true)
        if (openFirstTime) {
            startActivity(Intent(this, OpenFirstTimeActivity::class.java))
            this.finish()
        }
        setContent {
            MiniAccountTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)
                    var showNewBillDialogFlag by remember{
                        viewModel.showNewBill
                    }
                    val indexAccounts = remember {
                        viewModel.indexAccountList
                    }
                    val indexBillList = remember {
                        viewModel.indexBillList
                    }
                    val selectAccountList = remember {
                        viewModel.selectAccountList
                    }
                    //MainPage()
                    //AccountDetailPage()
                    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                        rememberTopAppBarState()
                    )
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            LargeTopAppBar(
                                title = {
                                    Column {
                                        val userName = getSharedPreferences("MiniAccount", Context.MODE_PRIVATE).getString("userName","")
                                        Text(
                                            "你好$userName",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontFamily = FontFamily(Font(R.font.qiantumakeshouxieti_2))
                                        )
                                        Text(
                                            "欢迎回来！",
                                            maxLines = 1,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontFamily = FontFamily(Font(R.font.qiantumakeshouxieti_2))
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                actions = {
                                    IconButton(onClick = { /*TODO 点击展开设置菜单*/ }) {
                                        Icon(imageVector = Icons.Filled.Menu,
                                            contentDescription = "菜单" )
                                    }
                                },
                                scrollBehavior = scrollBehavior
                            )
                        },
                        floatingActionButton ={
                            FloatingActionButton(onClick = {
                                showNewBillDialogFlag = !showNewBillDialogFlag
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        },
                    ) {innerPadding ->
                        var showDetailBill by remember { mutableStateOf(
                            if (indexBillList.isEmpty())BillEntity(1999,1,1,"一",true,0,"",0.0f,"")
                            else indexBillList[0]) }
                        Column{
                            var showDetailDialogFlag by remember {
                                viewModel.showDetailDialogFlag
                            }
                            LazyColumn(contentPadding = innerPadding, modifier = Modifier.padding(horizontal = 16.dp)) {
                                item{
                                    ElevatedCard(
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                        onClick = { val intent = Intent(this@MainActivity,AccountListActivity::class.java)
                                            startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp), Arrangement.spacedBy(8.dp)
                                        ) {
                                            Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
                                                Icon(painter = painterResource(id = R.drawable.funds), contentDescription = "余额图标")
                                                Text(
                                                    text = "账户余额",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                            Divider()
                                            repeat(indexAccounts.size) { index ->
                                                val accountItem = indexAccounts[index]
                                                Row(
                                                    Modifier
                                                        .padding(horizontal = 5.dp)
                                                        .fillMaxWidth(), Arrangement.SpaceBetween) {
                                                    if(accountItem.accountType=="储蓄")
                                                        Icon(painter = painterResource(id = R.drawable.bank_card_one), contentDescription = "银行卡图标",)
                                                    else Icon(painter = painterResource(id = R.drawable.credit), contentDescription = "信用卡")
                                                    Text(text = accountItem.accountName)
                                                    Text(text = accountItem.accountType)
                                                    Text(
                                                        text = accountItem.balance.toString())
                                                }
                                            }
                                            if (indexAccounts.isEmpty())
                                                Row (modifier = Modifier.fillMaxWidth(), Arrangement.Center){
                                                    Text(text = "你还没有添加账户信息，点我添加账户！")
                                                }
                                        }
                                    }
                                }
                                val yearPanel = indexBillList.distinctBy { it.yearStr }
                                items(yearPanel.size){yearIndex->
                                    Text(text = ("${yearPanel[yearIndex].yearStr}年"))
                                    val yearList = indexBillList.filter { it.yearStr == yearPanel[yearIndex].yearStr }
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
                                                                            ), contentDescription = "支出箭头", tint = MaterialTheme.colorScheme.primaryContainer)
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
                                                        Text(text = "支出：￥-$out_pay", color = MaterialTheme.colorScheme.onPrimaryContainer)
                                                        Text(text = "收入：￥$in_come", modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.error, textAlign = TextAlign.End)
                                                    }
                                                    Text(text = "总结：￥${in_come-out_pay}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                                                }
                                            }
                                        }

                                    }
                                    }

                                }
                                if(indexBillList.size > 10)
                                    item{
                                        Button(modifier = Modifier
                                            .padding(horizontal = 26.dp, vertical = 16.dp)
                                            .fillMaxWidth(),
                                            onClick = { startActivity(Intent(this@MainActivity,BillListActivity::class.java)) }) {
                                            Text(text = "查看所有账单")
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
                                                    IconButton(onClick = { viewModel.removeBill(showDetailBill) }) {
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
                            if (showNewBillDialogFlag){
                                Dialog(onDismissRequest = { showNewBillDialogFlag = !showNewBillDialogFlag }) {
                                    Card (modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max),
                                        shape = RoundedCornerShape(16.dp)){
                                        Column (Modifier.padding(bottom = 16.dp)){
                                            Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)){
                                                Row (modifier = Modifier
                                                    .padding(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "添加新的账单")
                                                    Text(modifier = Modifier
                                                        .weight(1.0f),
                                                        text = "添加新的账单",
                                                        textAlign = TextAlign.Center,
                                                        style = MaterialTheme.typography.titleMedium)
                                                    IconButton(onClick = { showNewBillDialogFlag = !showNewBillDialogFlag }) {
                                                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "关闭窗口")
                                                    }
                                                }
                                            }
                                            Divider()
                                            Column (
                                                Modifier
                                                    .weight(1.0f)
                                                    .padding(16.dp, 8.dp),Arrangement.spacedBy(8.dp)){
                                                var accountSelect by remember {
                                                    mutableStateOf(
                                                        if (selectAccountList.isEmpty()) AccountEntity("",0f,"","",true)
                                                        else selectAccountList[0]) }
                                                var selectedType by remember { mutableStateOf(true) }
                                                var balanceStr by remember { mutableStateOf("") } /*使用时需要toFloat() */
                                                var inputText by remember {
                                                    mutableStateOf("")
                                                }
                                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    var expanded by remember { mutableStateOf(false) }
                                                    Box{
                                                       Button(onClick = { expanded = true }) {
                                                           Text(text = accountSelect.accountName)
                                                       }
                                                        DropdownMenu(
                                                            expanded = expanded,
                                                            onDismissRequest = { expanded=false }) {
                                                            repeat(selectAccountList.size){
                                                                val item = selectAccountList[it]
                                                                DropdownMenuItem(
                                                                    text = { Text(text = (item.accountName)) },
                                                                    onClick = {
                                                                        accountSelect = item
                                                                        expanded = false
                                                                    })
                                                            }
                                                        }
                                                    }
//                                                    OutlinedTextField(value = accountName, onValueChange = {accountName=it},
//                                                        singleLine = true,
//                                                        label = { Text(text = "账户名称")},
//                                                        placeholder = { Text(text = "XX银行")})
                                                }
                                                Row (modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement =Arrangement.SpaceEvenly){
                                                    FilterChip(
                                                        onClick = { selectedType = true},
                                                        label = {
                                                            Text("支出")
                                                        },
                                                        selected = selectedType,
                                                        leadingIcon = if (selectedType) {
                                                            {
                                                                Icon(
                                                                    imageVector = Icons.Filled.Done,
                                                                    contentDescription = "Done icon",
                                                                    modifier = Modifier.size(
                                                                        FilterChipDefaults.IconSize)
                                                                )
                                                            }
                                                        } else { null },
                                                    )
                                                    FilterChip(
                                                        onClick = { selectedType = false },
                                                        label = {
                                                            Text("收入")
                                                        },
                                                        selected = !selectedType,
                                                        leadingIcon = if (!selectedType) {
                                                            {
                                                                Icon(
                                                                    imageVector = Icons.Filled.Done,
                                                                    contentDescription = "Done icon",
                                                                    modifier = Modifier.size(
                                                                        FilterChipDefaults.IconSize)
                                                                )
                                                            }
                                                        } else { null },
                                                    )


                                                }
                                                Row {
                                                    OutlinedTextField(value = balanceStr, onValueChange = { balanceStr=it.replace(" ","")},
                                                        singleLine = true,
                                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                        label = { Text(text = "金额")})
                                                }
                                                Row (verticalAlignment = Alignment.Top){
                                                    OutlinedTextField(modifier = Modifier.height(150.dp), value = inputText, onValueChange = {inputText = it},
                                                        label = { Text(text = "备注")})
                                                }
                                                val mCalendar = Calendar.getInstance()
                                                mCalendar.timeInMillis=System.currentTimeMillis()
                                                val weekday = when(mCalendar.get(Calendar.DAY_OF_WEEK)){
                                                    1->"日"
                                                    2->"一"
                                                    3->"二"
                                                    4->"三"
                                                    5->"四"
                                                    6->"五"
                                                    7->"六"
                                                    else -> {""}
                                                }
                                                Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center){
                                                Button(onClick = {
                                                    val billAmount = if (balanceStr.isEmpty()) 0F else balanceStr.toFloatOrNull()
                                                    if (accountSelect.id == 0L){
                                                        Toast.makeText(this@MainActivity, "请先添加账户", Toast.LENGTH_SHORT).show()
                                                    }else if (billAmount == null){
                                                        Toast.makeText(this@MainActivity, "输入的金额不合理", Toast.LENGTH_SHORT).show()
                                                    }else if(billAmount <0.01){
                                                        Toast.makeText(this@MainActivity, "没有输入金额", Toast.LENGTH_SHORT).show()
                                                    } else{
                                                        val newBill = BillEntity(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONDAY)+1,
                                                            mCalendar.get(Calendar.DATE),weekday,selectedType,accountSelect.id,accountSelect.accountName,billAmount,inputText)
                                                        viewModel.addNewBill(newBill,accountSelect)
                                                    }
                                                }) { Text(text = "添加记录") }
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

    override fun onRestart() {
        super.onRestart()
        viewModel.refreshCard()
    }
}
