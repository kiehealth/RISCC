import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cronelab.riscc.R
import com.cronelab.riscc.support.AppController
import com.cronelab.riscc.support.callback.ClickCallback

/*
class GenericSpinnerAdapter(private val data: List<String>) : RecyclerView.Adapter<GenericSpinnerAdapter.DataViewHolder>() {
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var clickCallback: ClickCallback

    private fun setItemClick(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_generic_spinner_item, parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.itemView.spinnerTitleTV.text = data[position]
        holder.itemView.setOnClickListener {
            clickCallback.callback(position, "")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}*/

class GenericSpinnerAdapter(private val data: List<String>) : BaseAdapter() {
    private lateinit var clickCallback: ClickCallback

    fun setItemClickCallBack(clickCallBack: ClickCallback) {
        clickCallback = clickCallBack
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any? {
        return position
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val context = AppController.instance.applicationContext
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.row_generic_spinner_item, parent, false)
        val titleTV = rowView.findViewById<TextView>(R.id.spinnerItemTV)
        titleTV.text = data[position]
        titleTV.setOnClickListener { clickCallback.callback(position, "") }
        return rowView
    }
}