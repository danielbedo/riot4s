package com.github.danielbedo


object ApiErrors {

  sealed trait ApiError
  sealed trait HttpError extends ApiError
  sealed trait LibraryError extends ApiError
  sealed trait SummonerError extends ApiError


  case object BadRequest extends HttpError
  case object Forbidden extends HttpError
  case object NotFound extends HttpError
  case object UnsupportedMediaType extends HttpError
  case object HardRateLimitExceeded extends HttpError
  case object InternalServerError extends HttpError
  case object ServiceUnavailable extends HttpError

  case object SoftRateLimitExceeded extends LibraryError
  case object UnparseableJson extends LibraryError

  case object SummonerNotFound extends ApiError




}
