package com.sanlushibawan.miniaccount.page

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.sanlushibawan.miniaccount.R
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.page.viewModels.OpenFirstTimeViewModel
import com.sanlushibawan.miniaccount.ui.theme.MiniAccountTheme

class OpenFirstTimeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //.getBoolean("isFirstTime",true)
        setContent {
            MiniAccountTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer){
                    val openFirstTimeViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(OpenFirstTimeViewModel::class.java)
                    var nameInput by remember {
                        openFirstTimeViewModel.inputUserName
                    }
                    openFirstTimeViewModel.insertResLong.observe(this){

                        if (it!=0L) {
                            getSharedPreferences("MiniAccount", Context.MODE_PRIVATE).edit{
                                putBoolean("isFirstTime",false)
                                putString("userName",nameInput)
                            }
                            this.finish()
                        }
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 56.dp), horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "欢迎使用迷你钱包!",
                            style = TextStyle(
                                fontWeight = FontWeight.W800, //设置字体粗细
                                fontSize = 36.sp,
                                letterSpacing = 7.sp),
                            fontFamily = FontFamily(Font(R.font.qiantumakeshouxieti_2))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "首先进行一些基础设置")
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "如何称呼？")
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(value = nameInput, onValueChange = {nameInput = it},
                            label = { Text(text = "输入用户名")})
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "然后添加一个基础账户")
                        var accountName by remember { mutableStateOf("") }
                        var selectedType by remember { mutableStateOf(true) }
                        var balanceStr by remember { mutableStateOf("") } /*使用时需要toFloat() */
                        var accountId by remember { mutableStateOf("") }
                        var showInIndex by remember { mutableStateOf(true) }
                        Column(
                            Modifier
                                .padding(16.dp, 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Card(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary)
                            ){
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ){
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
                                        horizontalArrangement = Arrangement.SpaceEvenly){
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
                                                            FilterChipDefaults.IconSize))
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
                                        horizontalArrangement = Arrangement.SpaceEvenly){
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
                                }
                            }
                            Box (Modifier.fillMaxSize(), Alignment.Center){
                                Button(onClick = {
                                    if (nameInput == ""){
                                        Toast.makeText(this@OpenFirstTimeActivity, "还不知道如何称呼你", Toast.LENGTH_SHORT).show()
                                    }
                                    else if (accountName == ""){
                                        Toast.makeText(this@OpenFirstTimeActivity, "没有填写账户名称", Toast.LENGTH_SHORT).show()
                                    }else if (accountId == "" || accountId.length < 11){
                                        Toast.makeText(this@OpenFirstTimeActivity, "没有填写账号或账号过短", Toast.LENGTH_SHORT).show()
                                    }else if (balanceStr == "") balanceStr = "0"
                                    else {
                                        val typeStr = if (selectedType) "储蓄" else "信用"
                                        openFirstTimeViewModel.addAccount(AccountEntity(accountName,balanceStr.toFloat(),typeStr,accountId,showInIndex))
                                    }
                                }) {
                                    Text(text = "开始使用")
                                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "go")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}