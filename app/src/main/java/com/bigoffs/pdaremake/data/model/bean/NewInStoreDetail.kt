package com.bigoffs.pdaremake.data.model.bean

data class NewInStoreDetail(
    val barcode_sku_map: MutableMap<String,Int>,
    val in_store_list: InStoreList,
    val task_list: TaskList
)



data class InStoreList(
    val sku_list:  MutableMap<String,Int>,
    val unique_code_list: List<String>
)

data class TaskList(
    val sku_list:  MutableMap<String,Int>,
    val unique_code_list: List<String>
)

