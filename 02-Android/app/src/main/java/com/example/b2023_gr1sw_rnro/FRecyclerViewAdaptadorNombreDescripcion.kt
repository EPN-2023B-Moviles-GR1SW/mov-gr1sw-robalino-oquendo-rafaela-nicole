import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.b2023_gr1sw_rnro.BEntrenador
import com.example.b2023_gr1sw_rnro.FRecyclerView
import com.example.b2023_gr1sw_rnro.R

class FRecyclerViewAdaptadorNombreDescripcion (
    private val contexto: FRecyclerView,
    private val lista: ArrayList<BEntrenador>,
    private val recyclerView: RecyclerView
): RecyclerView.Adapter<
        FRecyclerViewAdaptadorNombreDescripcion.MyViewHolder
        >() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val nombreTextView: TextView
        val descripcionTextView: TextView
        val likesTextView: TextView
        val accionButton: Button
        var numeroLikes = 0

        init {
            nombreTextView = view.findViewById(R.id.tv_nombre)
            descripcionTextView = view.findViewById(R.id.tv_descripcion)
            likesTextView = view.findViewById(R.id.tv_likes)
            accionButton = view.findViewById(R.id.btn_dar_like)
            accionButton.setOnClickListener { anadirLike() }
        }

        fun anadirLike(){
            numeroLikes = numeroLikes + 1
            likesTextView.text = numeroLikes.toString()
            // contexto.aumentarTotalLikes()
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(
        parent:
        ViewGroup, viewType: Int
    ): FRecyclerViewAdaptadorNombreDescripcion.MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: FRecyclerViewAdaptadorNombreDescripcion.MyViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }
}
