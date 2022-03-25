package com.example.habitguru.databaseHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.habitguru.R
import com.example.habitguru.dataClasses.habit
import com.example.habitguru.dataClasses.health
import com.example.habitguru.dataClasses.task
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private lateinit var db: SQLiteDatabase

    // TABLE NAMES
    private val TABLE_NAME = "HABIT_TABLE"
    private val TASK_NAME = "TASK_TABLE"
    private val TABLE_NAME_DAILY_DATA = "DAILY_DATA_TABLE"
    private val TABLE_TARGET = "TARGET"
    private val TABLE_TARGET_DATA = "TARGET_DATA"

    // HABIT COLUMNS
    private val COL_1 = "ID"
    private val COL_2 = "TITLE"
    private val COL_3 = "TIME"
    private val COL_4 = "DURATION"
    private val COL_5 = "IMAGE_ID"
    private val COL_6 = "STATUS"


    // HEALTH COLUMNS
    private val COL_HEALTH_1 = "ID"
    private val COL_HEALTH_2 = "NAME"
    private val COL_HEALTH_3 = "PROGRESS"
    private val COL_HEALTH_4 = "TOTALPROGRESS"
    private val COL_HEALTH_5 = "IMG"
    private val COL_HEALTH_6 = "UNIT"


    // DAILY DATA COLUMNS
    private val COL_DAILY_11 = "DATE"
    private val COL_DAILY_1 = "ID"
    private val COL_DAILY_2 = "WAKE_UP"
    private val COL_DAILY_3 = "BRUSH"
    private val COL_DAILY_4 = "EXERCISE"
    private val COL_DAILY_5 = "TAKE_SHOWER"
    private val COL_DAILY_6 = "BREAKFAST"
    private val COL_DAILY_7 = "LUNCH"
    private val COL_DAILY_8 = "STUDY"
    private val COL_DAILY_9 = "SPORTS"
    private val COL_DAILY_10 = "SLEEP"


    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES_HABIT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT , TITLE TEXT , TIME TEXT, DURATION INTEGER, IMAGE_ID INTEGER, STATUS INTEGER )"

        val SQL_CREATE_ENTRIES_TASK =
            "CREATE TABLE IF NOT EXISTS $TASK_NAME(ID INTEGER PRIMARY KEY AUTOINCREMENT , TITLE TEXT , TIME TEXT, DURATION INTEGER, IMAGE_ID INTEGER, STATUS INTEGER )"

        val SQL_CREATE_ENTRIES_HABIT_DAILY_DATA =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME_DAILY_DATA(ID INTEGER PRIMARY KEY AUTOINCREMENT  , WAKE_UP INTEGER, BRUSH INTEGER, EXERCISE INTEGER, TAKE_SHOWER INTEGER, BREAKFAST INTEGER, LUNCH INTEGER, STUDY INTEGER, SPORTS INTEGER, SLEEP INTEGER , DATE TEXT)"

        val SQL_CREATE_ENTRIES_DASHBOARD =
            "CREATE TABLE IF NOT EXISTS $TABLE_TARGET(ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT, PROGRESS TEXT, TOTALPROGRESS TEXT, IMG TEXT, UNIT TEXT)"

        val SQL_CREATE_ENTRIES_DASHBOARD_DAILY_DATA =
            "CREATE TABLE IF NOT EXISTS $TABLE_TARGET_DATA(ID INTEGER PRIMARY KEY AUTOINCREMENT, WATER TEXT, WEIGHT TEXT, READ TEXT, COFFEE TEXT,  DATE TEXT)"



        db.execSQL(SQL_CREATE_ENTRIES_HABIT)
        db.execSQL(SQL_CREATE_ENTRIES_TASK)
        db.execSQL(SQL_CREATE_ENTRIES_DASHBOARD)
        db.execSQL(SQL_CREATE_ENTRIES_DASHBOARD_DAILY_DATA)
        db.execSQL(SQL_CREATE_ENTRIES_HABIT_DAILY_DATA)
    }

    fun insertHabit(model: habit): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model.title)
        values.put(COL_3, model.time.toString())
        values.put(COL_4, model.habit_duration)
        values.put(COL_5, model.img_id)
        values.put(COL_6, model.status)

        return db.insert(TABLE_NAME, null, values)

    }

    fun insertTask(model: task): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model.title)
        values.put(COL_3, model.time.toString())
        values.put(COL_4, model.habit_duration)
        values.put(COL_5, model.img_id)
        values.put(COL_6, model.status)

        return db.insert(TASK_NAME, null, values)

    }

    fun insertDate(date: String): Long {
        val db = this.writableDatabase
        val date_list = getTasksDateByDate(date)
        if (date_list.isEmpty()) {
            val values = ContentValues()
            values.put(COL_DAILY_11, date.toString())
            values.put(COL_DAILY_2, 0)
            values.put(COL_DAILY_3, 0)
            values.put(COL_DAILY_4, 0)
            values.put(COL_DAILY_5, 0)
            values.put(COL_DAILY_6, 0)
            values.put(COL_DAILY_7, 0)
            values.put(COL_DAILY_8, 0)
            values.put(COL_DAILY_9, 0)
            values.put(COL_DAILY_10, 0)


            val values_target = ContentValues()
            values_target.put("WATER", 0)
            values_target.put("WEIGHT", 0)
            values_target.put("READ", 0)
            values_target.put("COFFEE", 0)
            values_target.put("DATE", date)

            if (this.get_health().isEmpty()) {

                val health_cards = ArrayList<com.example.habitguru.dataClasses.health>()
                health_cards.add(
                    com.example.habitguru.dataClasses.health(
                        "WATER",
                        R.drawable.water_glass, "0", "12", "glasses"
                    )
                )
                health_cards.add(
                    com.example.habitguru.dataClasses.health(
                        "WEIGHT",
                        R.drawable.kissclipart_weight_loss_icon_clipart_weight_loss_computer_icon_ae31145f3709a7d2,
                        "0",
                        "50",
                        "Kg"
                    )
                )
                health_cards.add(
                    com.example.habitguru.dataClasses.health(
                        "READ",
                        R.drawable.read, "0", "5", "Chapters"
                    )
                )
                health_cards.add(
                    com.example.habitguru.dataClasses.health(
                        "COFFEE",
                        R.drawable.chai, "0", "2", "Cups"
                    )
                )

                for (i in health_cards) {
                    insertHealth(i)
                }
            }



            db.insert(TABLE_TARGET_DATA, null, values_target)


            return db.insert(TABLE_NAME_DAILY_DATA, null, values)

        } else {
            return -1
        }


    }

    fun insertHealth(health: health): Long {

        val db = this.writableDatabase
        val values_health = ContentValues()
        values_health.put(COL_HEALTH_2, health.title)
        values_health.put(COL_HEALTH_3, health.curProgress)
        values_health.put(COL_HEALTH_4, health.totalProgress)
        values_health.put(COL_HEALTH_5, health.img)
        values_health.put(COL_HEALTH_6, health.unit)

        return db.insert(TABLE_TARGET, null, values_health)
    }

    @SuppressLint("Range")
    fun getAllTasksDate(): ArrayList<HashMap<String, String>>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<HashMap<String, String>> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME_DAILY_DATA, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(
                            "ID",
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_2,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_2)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_3,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_4,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_4)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_5,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_5)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_6,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_6)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_7,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_7)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_8,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_8)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_9,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_9)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_10,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_10)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_11,
                            cursor.getString(cursor.getColumnIndex(COL_DAILY_11))
                        )
                        modelList.add(hashMap)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }


    fun updateDate(date: String, task: String) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(task, 1)
        db.update(TABLE_NAME_DAILY_DATA, values, "DATE=?", arrayOf(date.toString()))
    }

    fun updateDateHealth(date: String, task: String, value: String) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(task, value)
        db.update(TABLE_TARGET_DATA, values, "DATE=?", arrayOf(date.toString()))
    }

    @SuppressLint("Range")
    fun getAllTasksDate_Dashboard(): ArrayList<HashMap<String, String>>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val modelList: ArrayList<HashMap<String, String>> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_TARGET_DATA WHERE DATE = '$currentDate'", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(
                            "ID",
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put("WATER", cursor.getString(cursor.getColumnIndex("WATER")))
                        hashMap.put("WEIGHT", cursor.getString(cursor.getColumnIndex("WEIGHT")))
                        hashMap.put("READ", cursor.getString(cursor.getColumnIndex("READ")))
                        hashMap.put("COFFEE", cursor.getString(cursor.getColumnIndex("COFFEE")))
                        hashMap.put("DATE", cursor.getString(cursor.getColumnIndex("DATE")))

                        modelList.add(hashMap)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }


    @SuppressLint("Range")
    fun get_health_by_name(name: String): health {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<health> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_TARGET WHERE NAME = '$name'", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            health(
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_2)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_5)).toInt(),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_3)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_4)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList[0]
    }

    @SuppressLint("Range")
    fun get_health(): ArrayList<health> {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<health> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_TARGET", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            health(
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_2)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_5)).toInt(),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_3)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_4)),
                                cursor.getString(cursor.getColumnIndex(COL_HEALTH_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }


    @SuppressLint("Range")
    fun Target_Dashboard(): ArrayList<HashMap<String, String>>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<HashMap<String, String>> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.query(TABLE_TARGET, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(
                            "ID",
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put(
                            COL_HEALTH_2,
                            cursor.getString(cursor.getColumnIndex(COL_HEALTH_2))
                        )
                        hashMap.put(
                            COL_HEALTH_3,
                            cursor.getString(cursor.getColumnIndex(COL_HEALTH_3))
                        )
                        hashMap.put(
                            COL_HEALTH_4,
                            cursor.getString(cursor.getColumnIndex(COL_HEALTH_4))
                        )
                        hashMap.put(
                            COL_HEALTH_5,
                            cursor.getString(cursor.getColumnIndex(COL_HEALTH_5))
                        )

                        modelList.add(hashMap)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }

    @SuppressLint("Range")
    fun getTasksDateByDate(date: String): ArrayList<HashMap<String, String>> {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<HashMap<String, String>> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_DAILY_DATA WHERE DATE = '$date'", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(
                            "ID",
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_2,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_2)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_3,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_1)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_4,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_4)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_5,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_5)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_6,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_6)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_7,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_7)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_8,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_8)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_9,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_9)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_10,
                            cursor.getInt(cursor.getColumnIndex(COL_DAILY_10)).toString()
                        )
                        hashMap.put(
                            COL_DAILY_11,
                            cursor.getString(cursor.getColumnIndex(COL_DAILY_11))
                        )
                        modelList.add(hashMap)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }

    @SuppressLint("Range")
    fun getTasksDateByTask(task: String, date: String): HashMap<String, String> {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<HashMap<String, String>> = ArrayList()
        db.beginTransaction()
        try {
            cursor =
                db.rawQuery("SELECT ${task} FROM $TABLE_NAME_DAILY_DATA WHERE DATE = '$date'", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(task, cursor.getInt(cursor.getColumnIndex(task)).toString())
                        modelList.add(hashMap)
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList[0]
    }

    fun updateTask(id: Int, model: task?) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model?.title)
        values.put(COL_3, model?.time.toString())
        values.put(COL_4, model?.habit_duration)
        values.put(COL_5, model?.img_id)
        values.put(COL_6, model?.status)
        db.update(TASK_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    fun updateHabit(id: Int, model: habit?) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_2, model?.title)
        values.put(COL_3, model?.time.toString())
        values.put(COL_4, model?.habit_duration)
        values.put(COL_5, model?.img_id)
        values.put(COL_6, model?.status)
        db.update(TABLE_NAME, values, "ID=?", arrayOf(id.toString()))
    }

    fun updateHealth(name:String, value:Int) {
        db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_HEALTH_4, value)
        db.update(TABLE_TARGET, values, "NAME=?", arrayOf(name))
    }

    @SuppressLint("Range")
    fun getProjectByName(name: String?): habit {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<habit> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE TITLE = '$name'", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            habit(
                                cursor.getInt(cursor.getColumnIndex(COL_1)),
                                cursor.getString(cursor.getColumnIndex(COL_2)),
                                Time.valueOf(cursor.getString(cursor.getColumnIndex(COL_3))),
                                cursor.getInt(cursor.getColumnIndex(COL_4)),
                                cursor.getInt(cursor.getColumnIndex(COL_5)),
                                cursor.getInt(cursor.getColumnIndex(COL_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList[0]
    }

    @SuppressLint("Range")
    fun getProjectById(name: Long?): habit {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<habit> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE ID = $name", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            habit(
                                cursor.getInt(cursor.getColumnIndex(COL_1)),
                                cursor.getString(cursor.getColumnIndex(COL_2)),
                                Time.valueOf(cursor.getString(cursor.getColumnIndex(COL_3))),
                                cursor.getInt(cursor.getColumnIndex(COL_4)),
                                cursor.getInt(cursor.getColumnIndex(COL_5)),
                                cursor.getInt(cursor.getColumnIndex(COL_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList[0]
    }

    @SuppressLint("Range")
    fun getTasktById(name: Long?): task {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<task> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TASK_NAME WHERE ID = $name", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            task(
                                cursor.getInt(cursor.getColumnIndex(COL_1)),
                                cursor.getString(cursor.getColumnIndex(COL_2)),
                                cursor.getString(cursor.getColumnIndex(COL_3)),
                                cursor.getInt(cursor.getColumnIndex(COL_4)),
                                cursor.getInt(cursor.getColumnIndex(COL_5)),
                                cursor.getInt(cursor.getColumnIndex(COL_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList[0]
    }

    fun deleteTask(id: Int) {
        db = this.writableDatabase
        db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getAllHabits(): ArrayList<habit>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<habit> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            habit(
                                cursor.getInt(cursor.getColumnIndex(COL_1)),
                                cursor.getString(cursor.getColumnIndex(COL_2)),
                                Time.valueOf(cursor.getString(cursor.getColumnIndex(COL_3))),
                                cursor.getInt(cursor.getColumnIndex(COL_4)),
                                cursor.getInt(cursor.getColumnIndex(COL_5)),
                                cursor.getInt(cursor.getColumnIndex(COL_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }

    @SuppressLint("Range")
    fun getAllTasks(): ArrayList<task>? {
        db = this.writableDatabase
        var cursor: Cursor? = null
        val modelList: ArrayList<task> = ArrayList()
        db.beginTransaction()
        try {
            cursor = db.query(TASK_NAME, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        modelList.add(
                            task(
                                cursor.getInt(cursor.getColumnIndex(COL_1)),
                                cursor.getString(cursor.getColumnIndex(COL_2)),
                                cursor.getString(cursor.getColumnIndex(COL_3)),
                                cursor.getInt(cursor.getColumnIndex(COL_4)),
                                cursor.getInt(cursor.getColumnIndex(COL_5)),
                                cursor.getInt(cursor.getColumnIndex(COL_6))
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        } finally {
            db.endTransaction()
            cursor?.close()
        }
        return modelList
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TASK_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_DAILY_DATA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TARGET")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TARGET_DATA")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 12
        private val DATABASE_NAME = "PROJECT_DATABASE"
    }
}