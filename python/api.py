# -*- coding: utf-8 -*-

import requests
import json
import hashlib
import hmac
from urlparse import parse_qs
from binascii import b2a_base64
import urllib

from vitadock.utils import generate_timestamp, generate_nonce


class VitadockOauthClient(object):
	API_ENDPOINT = "https://cloud.vitadock.com"

	request_token_url = "%s/auth/unauthorizedaccesses" % API_ENDPOINT
	access_token_url = "%s/auth/accesses/verify" % API_ENDPOINT
	authorization_url = "%s/desiredaccessrights/request" % API_ENDPOINT

	def __init__(self, app_key, app_secret, request_token=None,request_token_secret=None):
		self.app_key = app_key
		self.app_secret = app_secret
		self.request_token = request_token
		self.request_token_secret = request_token_secret
		return
		# return self.app_key

	def getRequestTokens(self):
		h = hashlib.sha256
		nonce = generate_nonce()

		data = {}
		data['oauth_consumer_key'] = self.app_key
		data['oauth_signature_method'] = "HMAC-SHA256"
		data['oauth_timestamp'] = str(generate_timestamp())
		data['oauth_nonce'] = nonce
		data['oauth_version'] = "1.0"

		base_string = []
		for k in sorted(data.keys()):
			base_string.append(k + urllib.quote("=",'') +urllib.quote(data[k],''))

		base_string = "POST&" + urllib.quote(self.request_token_url,'') + "&" + urllib.quote("&",'').join(base_string)
		sign = hmac.new(self.app_secret+"&", base_string, h)
		sign = b2a_base64(sign.digest()).strip().decode('utf-8')

		data2 = []
		for k in sorted(data.keys()):
			data2.append(k + "=\"" +data[k] + "\"")

		data2.append('oauth_signature="' + urllib.quote(sign,'') + '"')

		data2 = "OAuth " + ",".join(data2)
		data3 = {}
		data3['Authorization'] = data2


		rs = requests.post(self.request_token_url, headers=data3)
		rs = parse_qs(rs.text)

		request_token = rs['oauth_token'][0]
		request_token_secret = rs['oauth_token_secret'][0]
		self.request_token = request_token
		self.request_token_secret = request_token_secret
		return request_token, request_token_secret

	def getAccessTokens(self, verifier):
		if self.request_token is None:
			return "no request_token"
		if self.request_token_secret is None:
			return "no request_token_secret"
		h = hashlib.sha256
		nonce = generate_nonce()

		data = {}
		data['oauth_consumer_key'] = self.app_key
		data['oauth_signature_method'] = "HMAC-SHA256"
		data['oauth_timestamp'] = str(generate_timestamp())
		data['oauth_nonce'] = nonce
		data['oauth_version'] = "1.0"
		data['oauth_token'] = self.request_token
		data['oauth_verifier'] = verifier

		base_string = []
		for k in sorted(data.keys()):
			base_string.append(k + urllib.quote("=",'') +urllib.quote(data[k],''))

		base_string = "POST&" + urllib.quote(self.access_token_url,'') + "&" + urllib.quote("&",'').join(base_string)
		new_key = self.app_secret + "&" + self.request_token_secret
		sign = hmac.new(new_key, base_string, h)
		sign = b2a_base64(sign.digest()).strip().decode('utf-8')

		data2 = []
		for k in sorted(data.keys()):
			data2.append(k + "=\"" +data[k] + "\"")

		data2.append('oauth_signature="' + urllib.quote(sign,'') + '"')

		data2 = "OAuth " + ",".join(data2)
		data3 = {}
		data3['Authorization'] = data2


		rs = requests.post(self.access_token_url, headers=data3)

		rs = parse_qs(rs.text)
		access_token = rs['oauth_token'][0]
		access_token_secret = rs['oauth_token_secret'][0]

		self.access_token = access_token
		self.access_token_secret = access_token_secret

		return access_token, access_token_secret			
