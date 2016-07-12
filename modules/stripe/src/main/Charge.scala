package lila.stripe

import org.joda.time.DateTime
import ornicar.scalalib.Random

case class Charge(
  _id: String, // random
  userId: Option[String],
  stripe: Option[Charge.Stripe] = none,
  payPal: Option[Charge.PayPal] = none,
  cents: Cents,
  date: DateTime)

object Charge {

  def make(
    userId: Option[String],
    stripe: Option[Charge.Stripe] = none,
    payPal: Option[Charge.PayPal] = none,
    cents: Cents) = Charge(
    _id = Random nextStringUppercase 8,
    userId = userId,
    stripe = stripe,
    payPal = payPal,
    cents = cents,
    date = DateTime.now)

  case class Stripe(customerId: String) extends AnyVal
  case class PayPal(email: Option[String], subId: Option[String])

  private[stripe] object BSONHandlers {
    import reactivemongo.bson._
    import lila.db.dsl._
    implicit val CentsBSONHandler = intAnyValHandler[Cents](_.value, Cents.apply)
    implicit val StripeBSONHandler = Macros.handler[Stripe]
    implicit val PayPalBSONHandler = Macros.handler[PayPal]
    implicit val ChargeBSONHandler = Macros.handler[Charge]
  }
}
