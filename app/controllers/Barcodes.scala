package controllers

import javax.inject.{Singleton, Inject}

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

@Singleton
class Barcodes @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val ImageResolution = 144

  def barcode(ean: Long) = Action {

    import java.lang.IllegalArgumentException

    val MimeType = "image/png"
    try {
      val imageData = ean13BarCode(ean, MimeType)
      Ok(imageData).as(MimeType)
    }
    catch {
      case e: IllegalArgumentException =>
        BadRequest("Couldn't generate bar code. Error: " + e.getMessage)
    }
  }

  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {
    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage
    import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
    import org.krysalis.barcode4j.impl.upcean.EAN13Bean

    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    val canvas: BitmapCanvasProvider = new BitmapCanvasProvider(output, mimeType, ImageResolution, BufferedImage.TYPE_BYTE_BINARY, false, 0)

    val barcode = new EAN13Bean()
    barcode.generateBarcode(canvas, String valueOf ean)
    canvas.finish

    output.toByteArray
  }

}
