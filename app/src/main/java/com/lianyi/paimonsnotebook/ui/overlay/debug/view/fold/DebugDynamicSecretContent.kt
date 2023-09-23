package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService

@Composable
fun DebugDynamicSecretContent(
    dsSaltTypes: Map<DynamicSecret.SaltType, String>,
    dsVersions: Map<DynamicSecret.Version, String>,
) {
    var result by remember {
        mutableStateOf("")
    }
    var query by remember {
        mutableStateOf("")
    }
    var body by remember {
        mutableStateOf("")
    }
    var showDsType by remember {
        mutableStateOf(false)
    }
    var showDsVersion by remember {
        mutableStateOf(false)
    }
    var currentType by remember {
        mutableStateOf(DynamicSecret.SaltType.X4)
    }
    var currentVersion by remember {
        mutableStateOf(DynamicSecret.Version.Gen1)
    }
    var includeChars by remember {
        mutableStateOf(false)
    }

    Text(text = result, fontSize = 18.sp)

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "Version = ${currentVersion.name} , Type = ${currentType.name} HoyolabVersion = ${CoreEnvironment.XrpcVersion}",
            fontSize = 18.sp
        )
    }

    Spacer(modifier = Modifier.height(5.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "q:", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        InputTextFiled(
            value = query,
            onValueChange = {
                query = it
            }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "b:", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        InputTextFiled(value = body, onValueChange = {
            body = it
        })
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = includeChars,
            onCheckedChange = {
                includeChars = it
            }
        )
        Text(text = "includeChars", fontSize = 18.sp)
    }

    Spacer(modifier = Modifier.height(5.dp))

    Row {
        Button(onClick = {
            result = DynamicSecret.getDynamicSecret(
                currentVersion,
                currentType,
                includeChars,
                query,
                body
            )
            SystemService.setClipBoardText(result)
        }) {
            Text(text = "生成&复制", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box {
            Button(onClick = {
                showDsType = true
            }) {
                Text(text = "DS类型", fontSize = 18.sp)
            }

            DropdownMenu(expanded = showDsType,
                onDismissRequest = { showDsType = false }) {
                dsSaltTypes.forEach { (k, _) ->
                    DropdownMenuItem(onClick = {
                        currentType = k
                        showDsType = false
                    }) {
                        Text(text = k.name, fontSize = 18.sp)
                    }
                }
            }

        }

        Spacer(modifier = Modifier.width(10.dp))

        Box {
            Button(onClick = {
                showDsVersion = true
            }) {
                Text(text = "DS版本", fontSize = 18.sp)
            }

            DropdownMenu(expanded = showDsVersion,
                onDismissRequest = { showDsVersion = false }) {
                dsVersions.forEach { (k, _) ->
                    DropdownMenuItem(onClick = {
                        currentVersion = k
                        showDsVersion = false
                    }) {
                        Text(text = k.name, fontSize = 18.sp)
                    }
                }
            }
        }
    }

    Row {
        Button(onClick = {
            val arr = intArrayOf(
                72, -106, -120, -90, 108, 108, -118, -6561, -112, 210,
                -72, -100, -86, -177147, -2187, -102, 168, -84, 120, 162,
                -102, -6561, -6561, -84, 192, 90, -110, -82, -118, -110,
                180, 108
            )
            val arr2 = intArrayOf(
                -122,
                -118,
                222,
                72,
                210,
                162,
                -59049,
                -78,
                -108,
                -72,
                -90,
                -122,
                -112,
                -84,
                -59049,
                -4782969,
                -19683,
                -1594323,
                168,
                72,
                -80,
                -120,
                84,
                -78,
                -122,
                114,
                -80,
                -110,
                204,
                96,
                -118,
                -90
            )
            val arr3 = intArrayOf(
                -729,
                96,
                -86,
                -88,
                -108,
                90,
                -74,
                54,
                186,
                84,
                -100,
                -104,
                -4782969,
                -72,
                -104,
                102,
                90,
                102,
                78,
                -100,
                -2187,
                -59049,
                -108,
                -84,
                186,
                -59049,
                -84,
                198,
                96,
                54,
                -116,
                -84
            )
            val res = DynamicSecret.getSalt(arr2)
            println(res)
        }) {
            Text(text = "GetSalt", fontSize = 18.sp)
        }
    }

}