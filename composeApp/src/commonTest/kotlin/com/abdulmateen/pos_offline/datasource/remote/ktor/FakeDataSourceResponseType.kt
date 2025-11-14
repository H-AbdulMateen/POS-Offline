package com.abdulmateen.pos_offline.datasource.remote.ktor

sealed class FakeDataSourceResponseType {

   data class Empty(val body: String) : FakeDataSourceResponseType()
   data class SuccessData(val content: String) : FakeDataSourceResponseType()

   data class Error(val errorMessage: String) : FakeDataSourceResponseType()
}