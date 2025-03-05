package com.cronelab.riscc.ui.answer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cronelab.riscc.R
import com.cronelab.riscc.support.constants.QuestionnaireType
import com.cronelab.riscc.support.data.database.table.answer.Answer
import com.cronelab.riscc.support.data.database.table.answer.AnswerOption
import kotlinx.android.synthetic.main.row_answer.view.*

class AnsweredAdapter(var answerList: List<Answer>) : RecyclerView.Adapter<AnsweredAdapter.AnswerViewHolder>() {
    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_answer, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answerList[position]
        holder.itemView.titleTV.text = answer.question?.title
        holder.itemView.detailTV.text = answer.question?.body

        when (answer.question?.questionType!!.title) {
            QuestionnaireType.DATA_INPUT -> {
                holder.itemView.answerTV.text = "Ans: ${answer.answer}"
            }

            QuestionnaireType.SELECT_ONE -> {
                if (!answer.answer.isNullOrEmpty()) {
                    holder.itemView.answerTV.text = "Ans: ${answer.answer}"
                } else {
                    val answerOptionList = answer.answerOptions
                    holder.itemView.answerTV.text = prepareAnswer(answerOptionList)
                }
            }
            QuestionnaireType.SELECT_MORE_THAN_ONE -> {
                if (!answer.answer.isNullOrEmpty()) {
                    holder.itemView.answerTV.text = "Ans: ${answer.answer}"
                } else {
                    val answerOptionList = answer.answerOptions
                    holder.itemView.answerTV.text = prepareAnswer(answerOptionList)
                }
            }

            QuestionnaireType.RATING -> {
                if (!answer.answer.isNullOrEmpty()) {
                    holder.itemView.answerTV.text = "Ans: ${answer.answer}"
                } else {
                    val answerOptionList = answer.answerOptions
                    holder.itemView.answerTV.text = prepareAnswer(answerOptionList)
                }
            }
        }
    }

    private fun prepareAnswer(answerOptionList: List<AnswerOption>?): String {
        var answerTxt = "Ans: "
        if (answerOptionList != null) {
            for (option in answerOptionList) {
                answerTxt = answerTxt + option.title + ",  "
            }
        }
        return answerTxt
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    fun updateAnswer(answerList: List<Answer>) {
        this.answerList = answerList
        notifyDataSetChanged()
    }


}