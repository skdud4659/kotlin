package com.example.calculate.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calculate.model.History

// room에 연결된 dao
@Dao
interface HistoryDao {
    // history 가져오기
    @Query("SELECT * FROM history")
    fun getAll():List<History>

    // 저장
    @Insert
    fun insertHistory(history:History)

    // 삭제(전부)
    @Query("DELETE FROM history")
    fun delAll()

//    // 삭제(일부)
//    @Delete
//    fun delete(history: History)
//
//    // 특정 조건에 부합하는 요소만
//    @Query("SELECT * FROM history WHERE result LIKE :result")
//    fun findByResult(result: String): List<History>
//
//    // 특정 조건에 부합하는 요소 하나만
//    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
//    fun findByResultOne(result: String): History
}