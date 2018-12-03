package example

import java.io._
import com.github.tototoshi.csv._

object ExportData
{
  def AsCsv(name: String, header: List[String], rows: List[List[String]]) =
  {
    val f = new File(name)
    val writer = CSVWriter.open(f)
    
    writer.writeRow(header)
    
    for(row <- rows)
    {
      writer.writeRow(row)
    }
    
    writer.close()
  }
}