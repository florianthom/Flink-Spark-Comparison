package example

object Processing
{
  def extractAttributes(line:String, position:List[Int]): List[String] = line.split(", ")
    .zipWithIndex
    .filter(p => position.contains(p._2))
    .map(f => f._1)
    .toList
    
//  def vectorizeAttributes(attributes:List[String], dicts:List[Map[String, Int]]): Vector = Vectors.dense(dicts.zip(attributes)
//    .map(f => f._1(f._2).toDouble)
//    .toArray
//  )
    
  def degrees: Map[String, Int] = "Bachelors, Some-college, 11th, HS-grad, Prof-school, Assoc-acdm, Assoc-voc, 9th, 7th-8th, 12th, Masters, 1st-4th, 10th, Doctorate, 5th-6th, Preschool"
    .split(", ")
    .zipWithIndex
    .toMap
    
  def race: Map[String, Int] = "White, Asian-Pac-Islander, Amer-Indian-Eskimo, Other, Black"
    .split(", ")
    .zipWithIndex
    .toMap
}