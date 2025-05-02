
import com.savvasdalkitsis.jsonmerger.JsonMerger
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.file.Paths
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

open class BootstrapTask : DefaultTask() {

    private fun formatDate(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    private fun hash(file: ByteArray): String {
        return MessageDigest.getInstance("SHA-512").digest(file).fold("", { str, it -> str + "%02x".format(it) }).uppercase(Locale.getDefault())
    }

    /*
    private fun getBootstrap(filename: String): JSONArray? {
        val bootstrapFile = File(filename).readLines()

        return JSONObject("{\"plugins\":$bootstrapFile}").getJSONArray("plugins")
    }
    */


    private fun getBootstrap(): JSONArray? {
        val client = OkHttpClient()

        val url = "https://raw.githubusercontent.com/OreoCupcakes/kotori-plugins-releases/master/plugins.json"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response -> return JSONObject("{\"plugins\":${response.body!!.string()}}").getJSONArray("plugins") }
    }


    @TaskAction
    fun boostrap() {
        if (project == project.rootProject) {
            val bootstrapDir = File("${project.projectDir}/bootstrap")
            val bootstrapReleaseDir = File("${project.projectDir}/bootstrap/release")

            bootstrapDir.mkdirs()
            bootstrapReleaseDir.mkdirs()

            val plugins = ArrayList<JSONObject>()
        //    val baseBootstrap = getBootstrap("$bootstrapDir/plugins.json") ?: throw RuntimeException("Base bootstrap is null!")
            val baseBootstrap = getBootstrap() ?: throw RuntimeException("Base bootstrap is null!")

            project.subprojects.forEach {
                if (it.project.properties.containsKey("PluginName") && it.project.properties.containsKey("PluginDescription")
                    && it.project.properties.containsKey("PluginPackageId") && it.project.properties.containsKey("PluginMainClassName")) {
                    var pluginAdded = false
                    val plugin = it.project.tasks["jar"].outputs.files.singleFile

                    val releases = ArrayList<JsonBuilder>()

                    releases.add(JsonBuilder(
                            "version" to it.project.version,
                            "requires" to ProjectVersions.apiVersion,
                            "date" to formatDate(Date()),
                            "url" to "https://raw.githubusercontent.com/OreoCupcakes/kotori-plugins-releases/master/release/${it.project.name}-${it.project.version}.jar",
                            "sha512sum" to hash(plugin.readBytes())
                    ))

                    val pluginObject = JsonBuilder(
                            "name" to it.project.extra.get("PluginName"),
                            "id" to nameToId(it.project.extra.get("PluginName") as String),
                            "description" to it.project.extra.get("PluginDescription"),
                            "packageId" to it.project.extra.get("PluginPackageId"),
                            "mainClassName" to it.project.extra.get("PluginMainClassName"),
                            "provider" to it.project.extra.get("PluginProvider"),
                            "projectUrl" to it.project.extra.get("ProjectSupportUrl"),
                            "releases" to releases.toTypedArray()
                    ).jsonObject()

                    for (i in 0 until baseBootstrap.length()) {
                        val item = baseBootstrap.getJSONObject(i)

                        if (item.get("id") != nameToId(it.project.extra.get("PluginName") as String)) {
                            continue
                        }

                        if (it.project.version.toString() in item.getJSONArray("releases").toString()) {
                            pluginAdded = true
                            plugins.add(item)
                            break
                        }

                        plugins.add(JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.MERGE_ARRAY).merge(item, pluginObject))
                        pluginAdded = true
                    }

                    if (!pluginAdded)
                    {
                        plugins.add(pluginObject)
                    }

                    plugin.copyTo(Paths.get(bootstrapReleaseDir.toString(), "${it.project.name}-${it.project.version}.jar").toFile())
                }
            }

            File(bootstrapDir, "plugins.json").printWriter().use { out ->
                out.println(plugins.toString())
            }
        }

    }
}
