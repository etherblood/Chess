CREATE TABLE public.account
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  username text NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.account
  OWNER TO chess;
  
CREATE TABLE public.emailaddress
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  address text NOT NULL,
  account_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailaddress
  OWNER TO chess;

CREATE INDEX emailaddress_account_id_idx ON public.emailaddress (account_id);
  
CREATE TABLE public.lobby
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  ispublic boolean NOT NULL,
  name text NOT NULL,
  owner_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.lobby
  OWNER TO chess;

CREATE INDEX lobby_owner_id_idx ON public.lobby (owner_id);
  
CREATE TABLE public.chatmessage
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  receiver_lobby_id uuid REFERENCES public.lobby (id) ON DELETE CASCADE,
  receiver_account_id uuid REFERENCES public.account (id) ON DELETE CASCADE,
  sender_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE,
  messagetext text NOT NULL,
  CONSTRAINT chatmessage_receiver_exactly_one CHECK ((receiver_lobby_id IS NULL) <> (receiver_account_id IS NULL))
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.chatmessage
  OWNER TO chess;

CREATE INDEX chatmessage_receiver_account_id_idx ON public.chatmessage (receiver_account_id);
CREATE INDEX chatmessage_receiver_lobby_id_idx ON public.chatmessage (receiver_lobby_id);
CREATE INDEX chatmessage_sender_id_idx ON public.chatmessage (sender_id);
CREATE INDEX chatmessage_created_idx ON public.chatmessage (created);

CREATE TYPE membershiptype AS ENUM ('INVITED', 'REQUESTED', 'MEMBER', 'BANNED');
  
CREATE TABLE public.lobbymembership
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  membershiptype membershiptype NOT NULL,
  account_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE,
  lobby_id uuid NOT NULL REFERENCES public.lobby (id) ON DELETE CASCADE,
  CONSTRAINT lobbymembership_account_id_lobby_id_ukey UNIQUE (account_id, lobby_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.lobbymembership
  OWNER TO chess;

CREATE INDEX lobbymembership_lobby_id_idx ON public.lobbymembership (lobby_id);
  

CREATE TABLE public.login
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  loginhandle text NOT NULL UNIQUE,
  password text NOT NULL,
  account_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.login
  OWNER TO chess;

CREATE INDEX login_account_id_idx ON public.login (account_id);
  
CREATE TYPE chessresult AS ENUM ('UNDECIDED', 'WHITE_VICTORY', 'BLACK_VICTORY', 'DRAW');

CREATE TABLE public.chessmatch
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  updated TIMESTAMP WITH TIME ZONE NOT NULL,
  version integer NOT NULL,
  ended TIMESTAMP WITH TIME ZONE,
  startfen text NOT NULL,
  started TIMESTAMP WITH TIME ZONE,
  black_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE,
  white_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE,
  result chessresult NOT NULL,
  CONSTRAINT chessmatch_ended_matches_result CHECK ((ended IS NULL) = (result = 'UNDECIDED')),
  CONSTRAINT chessmatch_ended_matches_started CHECK (ended IS NULL OR started IS NOT NULL)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.chessmatch
  OWNER TO chess;

CREATE INDEX chessmatch_white_id_idx ON public.chessmatch (white_id);
CREATE INDEX chessmatch_black_id_idx ON public.chessmatch (black_id);
CREATE INDEX chessmatch_started_idx ON public.chessmatch (started);
CREATE INDEX chessmatch_ended_idx ON public.chessmatch (ended);

  

CREATE TYPE chesssquare AS ENUM (
	'a1','b1','c1','d1','e1','f1','g1','h1',
	'a2','b2','c2','d2','e2','f2','g2','h2',
	'a3','b3','c3','d3','e3','f3','g3','h3',
	'a4','b4','c4','d4','e4','f4','g4','h4',
	'a5','b5','c5','d5','e5','f5','g5','h5',
	'a6','b6','c6','d6','e6','f6','g6','h6',
	'a7','b7','c7','d7','e7','f7','g7','h7',
	'a8','b8','c8','d8','e8','f8','g8','h8');
	
CREATE TYPE chessmovetype AS ENUM ('SIMPLE','PAWN_JUMP','EN_PASSANT','CASTLING','PROMOTION_KNIGHT','PROMOTION_BISHOP','PROMOTION_ROOK','PROMOTION_QUEEN');

CREATE TABLE public.matchmove
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  fromsquare chesssquare NOT NULL,
  moveindex integer NOT NULL CHECK (moveindex >= 0),
  tosquare chesssquare NOT NULL,
  movetype chessmovetype NOT NULL,
  match_id uuid NOT NULL REFERENCES public.chessmatch (id) ON DELETE CASCADE,
  CONSTRAINT matchmove_match_id_moveindex_ukey UNIQUE (match_id, moveindex)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.matchmove
  OWNER TO chess;



CREATE TABLE public.matchrequest
(
  id uuid PRIMARY KEY,
  created TIMESTAMP WITH TIME ZONE NOT NULL,
  match_id uuid NOT NULL REFERENCES public.chessmatch (id) ON DELETE CASCADE,
  player_id uuid NOT NULL REFERENCES public.account (id) ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.matchrequest
  OWNER TO chess;

CREATE INDEX matchrequest_player_id_idx ON public.matchrequest (player_id);
CREATE INDEX matchrequest_match_id_idx ON public.matchrequest (match_id);