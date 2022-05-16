package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class recyclerView : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_view)
    
    val carList = mutableListOf<Car>()
    for (i in 0..100) {
      carList.add(Car("" + i + "자동차", "" + i + "엔진"))
    }
    
    // 어답터 장착
    val recyclerView = findViewById<RecyclerView>(R.id.recycler)
    recyclerView.adapter = RecyclerViewAdapter(carList, LayoutInflater.from(this))
    
    // 뷰의 배치 관리
    // LinearLayoutManager > 가로/세로방향
    recyclerView.layoutManager = LinearLayoutManager(this)
//    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    
    // GridLayoutManager > 바둑판
//    recyclerView.layoutManager = GridLayoutManager(this, 2)
  }
}

class Car(val nthCar: String, val nthEngine: String)

class RecyclerViewAdapter (
  var carList: MutableList<Car>,
  var inflater: LayoutInflater
  
  // Adapter를 상속받아야함.
  // viewHolder를 adapter에게 알려줘야함. viewHolder는 inner class로
):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
  
  // ViewHolder를 상속받아야함.
  // inner class로 바꿔줘야 outer class의 매개변수에 접근할 수 없다.
  // 아이템뷰의 상세뷰 컴포넌트를 홀드한다.
  // click event는 여기서! 전체 itemView와 각 항목들을 다 가지고 있기 때문.
  inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val carImage: ImageView
    val nthCar: TextView
    val nthEngine: TextView
    init {
      carImage = itemView.findViewById(R.id.carImage)
      nthCar = itemView.findViewById(R.id.nthCar)
      nthEngine = itemView.findViewById(R.id.nthEngine)
      
      itemView.setOnClickListener {
        // 몇번째 itemView 요소인지 adapterPosition로 알 수 있다.
        val position: Int = adapterPosition
        val car = carList.get(position)
        
        Log.d("testt", car.nthCar)
      }
    }
  }
  
  // viewHolder 생성
  // 아이템뷰를 리턴
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = inflater.inflate(R.layout.car_item, parent, false)
    return ViewHolder(view)
  }
  
  // 데이터를 아이템뷰의 뷰컴포넌트와 묶는다.
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.nthCar.text = carList.get(position).nthCar
    holder.nthEngine.text = carList.get(position).nthEngine
  }
  
  // 전체 데이터의 크기(갯수)를 리턴.
  override fun getItemCount(): Int {
    return carList.size
  }
}