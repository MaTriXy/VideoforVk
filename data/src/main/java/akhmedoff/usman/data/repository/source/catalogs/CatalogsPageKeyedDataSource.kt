package akhmedoff.usman.data.repository.source.catalogs

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.CatalogDao
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.ResponseCatalog
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections.emptyList

class CatalogsPageKeyedDataSource(
    private val vkApi: VkApi,
    private val filters: String,
    private val catalogDao: CatalogDao
) : PageKeyedDataSource<String, Catalog>() {

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Catalog>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Catalog>) {
        vkApi.getCatalog(
            count = params.requestedLoadSize,
            itemsCount = 7,
            from = params.key,
            filters = filters
        ).enqueue(object : Callback<ResponseCatalog> {

            override fun onFailure(call: Call<ResponseCatalog>?, t: Throwable?) {
                Log.d(javaClass.simpleName, "ERROR: " + t.toString())
            }

            override fun onResponse(
                call: Call<ResponseCatalog>,
                response: Response<ResponseCatalog>
            ) {
                response.body()?.let {
                    callback.onResult(it.catalogs, it.next)
                }
            }
        })
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Catalog>
    ) {
        try {
            val response = vkApi.getCatalog(
                count = params.requestedLoadSize,
                itemsCount = 7,
                filters = filters
            ).execute()

            val items = response.body()?.catalogs ?: emptyList<Catalog>()

            callback.onResult(items, null, response.body()?.next)

        } catch (exception: Exception) {
            Log.d(javaClass.simpleName, exception.toString())
        }

    }
}