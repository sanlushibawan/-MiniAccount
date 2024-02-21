package com.sanlushibawan.miniaccount.page


import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.sanlushibawan.miniaccount.R
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.page.viewModels.AccountListViewModel
import com.sanlushibawan.miniaccount.ui.theme.MiniAccountTheme

class AccountListActivity : ComponentActivity() {
    private lateinit var viewModel:AccountListViewModel
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AccountListViewModel::class.java)
        setContent {
            MiniAccountTheme {
                Surface (modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    var showNewAccountDialog by remember {
                        mutableStateOf(false)
                    }
                    val showAccountDetail by remember {
                        viewModel.accountDetailDBDone
                    }
                    viewModel.insertResLong.observe(this) {
                        if (it != 0L) showNewAccountDialog = false
                    }
                    val accountList = remember {
                        viewModel.allAccountList
                    }
                    viewModel.loadAccountList()
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = { Text(text = "账户管理") }, navigationIcon = {
                                    IconButton(onClick = { this.finish() }) {
                                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                                    }
                                }

                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(onClick = { showNewAccountDialog = !showNewAccountDialog}) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    ){innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)){
                            LazyColumn {
                                items(accountList.size) { index ->
                                    val accountItem = accountList[index]
                                    ElevatedCard(
                                        onClick = {
                                            viewModel.modifyAccount.value = index
                                            viewModel.accountDetailDBDone.value = !viewModel.accountDetailDBDone.value },
                                        modifier = Modifier
                                            .padding(24.dp)
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min),
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                                    ) {
                                        Column(Modifier.background(
                                            if(accountItem.accountType=="储蓄") MaterialTheme.colorScheme.errorContainer
                                            else MaterialTheme.colorScheme.tertiaryContainer))
                                        {
                                            Box(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                                                Row(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                                                    Text(
                                                        text = accountItem.accountName,
                                                        modifier = Modifier.weight(1.0f)
                                                    )
                                                    Text(text = accountItem.accountType)
                                                }
                                            }
                                            Divider()
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                var visibleAccountNum by remember {
                                                    mutableStateOf(false)
                                                }
                                                val strText = accountItem.accountId
                                                Text(
                                                    text = if (visibleAccountNum) strText else strText.replaceRange(
                                                        4,
                                                        strText.length - 4,
                                                        "***"
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                                IconButton(onClick = {
                                                    visibleAccountNum = !visibleAccountNum
                                                }) {
                                                    if (visibleAccountNum) Icon(
                                                        painter = painterResource(id = R.drawable.visibility),
                                                        modifier = Modifier.size(24.dp),
                                                        contentDescription = "Visibility"
                                                    )
                                                    else Icon(
                                                        painter = painterResource(id = R.drawable.visibility_off),
                                                        modifier = Modifier.size(24.dp),
                                                        contentDescription = "Visibility_off"
                                                    )
                                                }
                                            }
                                            Text(
                                                text = accountItem.balance.toString(),
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        horizontal = 24.dp,
                                                        vertical = 16.dp
                                                    ), textAlign = TextAlign.End
                                            )
                                        }
                                    }
                                }
                            }
                            if(showAccountDetail) Dialog(onDismissRequest = { viewModel.accountDetailDBDone.value = !viewModel.accountDetailDBDone.value }) {
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
                                                Icon(imageVector = Icons.Filled.Info, contentDescription = "占位",
                                                    tint = MaterialTheme.colorScheme.primary)
                                                Text(modifier = Modifier
                                                    .weight(1.0f),
                                                    text = "修改账户",
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.titleMedium)
                                                IconButton(onClick = {viewModel.accountDetailDBDone.value = !viewModel.accountDetailDBDone.value}) {
                                                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "关闭窗口")
                                                }
                                            }
                                        }
                                        Divider()
                                        val clickAccount = accountList[viewModel.modifyAccount.value?:0]
                                        var editAccountName by remember { mutableStateOf(clickAccount.accountName) }
                                        var editSelectedType by remember { mutableStateOf(clickAccount.accountType == "储蓄") }
                                        var editBalanceStr by remember { mutableStateOf(clickAccount.balance.toString()) } /*使用时需要toFloat() */
                                        var editAccountId by remember { mutableStateOf(clickAccount.accountId) }
                                        var editShowInIndex by remember { mutableStateOf(clickAccount.showIndexPage) }
                                        Column(
                                            Modifier
                                                //.weight(1.0f)
                                                .padding(16.dp, 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)){
                                            Row {
                                                OutlinedTextField(value = editAccountName, onValueChange = {editAccountName=it},
                                                    singleLine = true,
                                                    label = { Text(text = "开户行/金融机构")},
                                                    placeholder = { Text(text = "XX银行")})
                                            }
                                            Row {
                                                OutlinedTextField(value = editAccountId, onValueChange = {editAccountId=it},
                                                    singleLine = true,
                                                    label = { Text(text = "账号")},
                                                    placeholder = { Text(text = "卡号 or 手机号")})
                                            }
                                            Row (modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement =Arrangement.SpaceEvenly){
                                                FilterChip(
                                                    onClick = { editSelectedType = true},
                                                    label = {
                                                        Text("储蓄")
                                                    },
                                                    selected = editSelectedType,
                                                    leadingIcon = if (editSelectedType) {
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
                                                    onClick = { editSelectedType = false },
                                                    label = {
                                                        Text("信用")
                                                    },
                                                    selected = !editSelectedType,
                                                    leadingIcon = if (!editSelectedType) {
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
                                                OutlinedTextField(value = editBalanceStr, onValueChange = { editBalanceStr=it.replace(" ","")},
                                                    singleLine = true,
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    label = { Text(text = "当前余额")})
                                            }
                                            Row (modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement =Arrangement.SpaceEvenly){
                                                FilterChip(
                                                    onClick = { editShowInIndex = true},
                                                    label = {
                                                        Text("首页展示")
                                                    },
                                                    selected = editShowInIndex,
                                                    leadingIcon = if (editShowInIndex) {
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
                                                    onClick = { editShowInIndex = false },
                                                    label = {
                                                        Text("首页隐藏")
                                                    },
                                                    selected = !editShowInIndex,
                                                    leadingIcon = if (!editShowInIndex) {
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
                                            Row (Modifier.fillMaxWidth(),Arrangement.Center){
                                                Button(onClick = {
//                                                    var accountName by remember { mutableStateOf("") }
//                                                    var selectedType by remember { mutableStateOf(true) }
//                                                    var balanceStr by remember { mutableStateOf("") } /*使用时需要toFloat() */
//                                                    var accountId by remember { mutableStateOf("") }
//                                                    var showInIndex by remember { mutableStateOf(true) }
                                                    if (editAccountName == ""){
                                                        Toast.makeText(this@AccountListActivity, "没有填写账户名称", Toast.LENGTH_SHORT).show()
                                                    }else if (editAccountId == "" || editAccountId.length < 11){
                                                        Toast.makeText(this@AccountListActivity, "没有填写账号或账号过短", Toast.LENGTH_SHORT).show()
                                                    }else if (editBalanceStr == "") editBalanceStr = "0"
                                                    else {
                                                        val typeStr = if (editSelectedType) "储蓄" else "信用"
                                                        val newAccount =AccountEntity(editAccountName,editBalanceStr.toFloat(),typeStr,editAccountId,editShowInIndex)
                                                        newAccount.id = clickAccount.id
                                                        viewModel.updateAccount(newAccount)
                                                    }
                                                }) {
                                                    Text(text = "修改")
                                                }
                                                Button(onClick = {
                                                    viewModel.deleteAccount(clickAccount)
                                                }) {
                                                    Text(text = "删除")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(showNewAccountDialog) Dialog(onDismissRequest = { showNewAccountDialog = !showNewAccountDialog },) {
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
                                                Icon(imageVector = Icons.Filled.Info, contentDescription = "占位",
                                                    tint = MaterialTheme.colorScheme.primary)
                                                Text(modifier = Modifier
                                                    .weight(1.0f),
                                                    text = "添加账户",
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.titleMedium)
                                                IconButton(onClick = { showNewAccountDialog = !showNewAccountDialog }) {
                                                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "关闭窗口")
                                                }
                                            }
                                        }
                                        Divider()
                                        var accountName by remember { mutableStateOf("") }
                                        var selectedType by remember { mutableStateOf(true) }
                                        var balanceStr by remember { mutableStateOf("") } /*使用时需要toFloat() */
                                        var accountId by remember { mutableStateOf("") }
                                        var showInIndex by remember { mutableStateOf(true) }
                                        Column(
                                            Modifier
                                                //.weight(1.0f)
                                                .padding(16.dp, 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)){
                                            Row {
                                                OutlinedTextField(value = accountName, onValueChange = {accountName=it},
                                                    singleLine = true,
                                                    label = { Text(text = "开户行/金融机构")},
                                                    placeholder = { Text(text = "XX银行")})
                                            }
                                            Row {
                                                OutlinedTextField(value = accountId, onValueChange = {accountId=it},
                                                    singleLine = true,
                                                    label = { Text(text = "账号")},
                                                    placeholder = { Text(text = "卡号 or 手机号")})
                                            }
                                            Row (modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement =Arrangement.SpaceEvenly){
                                                FilterChip(
                                                    onClick = { selectedType = true},
                                                    label = {
                                                        Text("储蓄")
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
                                                        Text("信用")
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
                                                    label = { Text(text = "当前余额")})
                                            }
                                            Row (modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement =Arrangement.SpaceEvenly){
                                                FilterChip(
                                                    onClick = { showInIndex = true},
                                                    label = {
                                                        Text("首页展示")
                                                    },
                                                    selected = showInIndex,
                                                    leadingIcon = if (showInIndex) {
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
                                                    onClick = { showInIndex = false },
                                                    label = {
                                                        Text("首页隐藏")
                                                    },
                                                    selected = !showInIndex,
                                                    leadingIcon = if (!showInIndex) {
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
                                            Row (Modifier.fillMaxWidth(),Arrangement.Center){
                                                Button(onClick = {
//                                                    var accountName by remember { mutableStateOf("") }
//                                                    var selectedType by remember { mutableStateOf(true) }
//                                                    var balanceStr by remember { mutableStateOf("") } /*使用时需要toFloat() */
//                                                    var accountId by remember { mutableStateOf("") }
//                                                    var showInIndex by remember { mutableStateOf(true) }
                                                    if (accountName == ""){
                                                        Toast.makeText(this@AccountListActivity, "没有填写账户名称", Toast.LENGTH_SHORT).show()
                                                    }else if (accountId == "" || accountId.length < 11){
                                                        Toast.makeText(this@AccountListActivity, "没有填写账号或账号过短", Toast.LENGTH_SHORT).show()
                                                    }else if (balanceStr == "") balanceStr = "0"
                                                    else {
                                                        val typeStr = if (selectedType) "储蓄" else "信用"
                                                        viewModel.addAccount(AccountEntity(accountName,balanceStr.toFloat(),typeStr,accountId,showInIndex))
                                                    }
                                                }) {
                                                    Text(text = "添加")
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
}