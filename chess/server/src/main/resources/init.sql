CREATE TABLE public.account
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  username text NOT NULL,
  CONSTRAINT account_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.account
  OWNER TO chess;

CREATE TYPE receivertype AS ENUM ('LOBBY', 'ACCOUNT');
  
CREATE TABLE public.chatmessage
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  receiver_id uuid NOT NULL,
  receivertype receivertype NOT NULL,
  sender_id uuid NOT NULL,
  messagetext text NOT NULL,
  CONSTRAINT chatmessage_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.chatmessage
  OWNER TO chess;

  
CREATE TABLE public.emailaddress
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  address text NOT NULL,
  account_id uuid NOT NULL,
  CONSTRAINT emailaddress_pkey PRIMARY KEY (id),
  CONSTRAINT emailaddress_account_id_fkey FOREIGN KEY (account_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailaddress
  OWNER TO chess;

  
CREATE TABLE public.lobby
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  ispublic boolean NOT NULL,
  name text NOT NULL,
  owner_id uuid NOT NULL,
  CONSTRAINT lobby_pkey PRIMARY KEY (id),
  CONSTRAINT lobby_owner_id_fkey FOREIGN KEY (owner_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.lobby
  OWNER TO chess;


CREATE TYPE membershiptype AS ENUM ('INVITED', 'REQUESTED', 'MEMBER', 'BANNED');
  
CREATE TABLE public.lobbymembership
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  membershiptype membershiptype NOT NULL,
  account_id uuid NOT NULL,
  lobby_id uuid NOT NULL,
  CONSTRAINT lobbymembership_pkey PRIMARY KEY (id),
  CONSTRAINT lobbymembership_lobby_id_fkey FOREIGN KEY (lobby_id)
      REFERENCES public.lobby (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT lobbymembership_account_id_fkey FOREIGN KEY (account_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT lobbymembership_account_id_lobby_id_ukey UNIQUE (account_id, lobby_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.lobbymembership
  OWNER TO chess;

  

CREATE TABLE public.login
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  loginhandle text NOT NULL,
  password text NOT NULL,
  account_id uuid NOT NULL,
  CONSTRAINT login_pkey PRIMARY KEY (id),
  CONSTRAINT login_account_id_fkey FOREIGN KEY (account_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT login_loginhandle_ukey UNIQUE (loginhandle)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.login
  OWNER TO chess;

  

CREATE TABLE public.chessmatch
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone NOT NULL,
  version integer NOT NULL,
  ended timestamp without time zone,
  startfen text NOT NULL,
  started timestamp without time zone,
  black_id uuid NOT NULL,
  white_id uuid NOT NULL,
  CONSTRAINT chessmatch_pkey PRIMARY KEY (id),
  CONSTRAINT chessmatch_white_id_fkey FOREIGN KEY (white_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT chessmatch_black_id_fkey FOREIGN KEY (black_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.chessmatch
  OWNER TO chess;

  

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
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  fromsquare chesssquare NOT NULL,
  moveindex integer NOT NULL,
  tosquare chesssquare NOT NULL,
  movetype chessmovetype NOT NULL,
  match_id uuid NOT NULL,
  CONSTRAINT matchmove_pkey PRIMARY KEY (id),
  CONSTRAINT matchmove_match_id_fkey FOREIGN KEY (match_id)
      REFERENCES public.chessmatch (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT matchmove_match_id_moveindex_ukey UNIQUE (match_id, moveindex),
  CONSTRAINT matchmove_moveindex_nonnegative CHECK (moveindex >= 0)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.matchmove
  OWNER TO chess;



CREATE TABLE public.matchrequest
(
  id uuid NOT NULL,
  created timestamp without time zone NOT NULL,
  match_id uuid NOT NULL,
  player_id uuid NOT NULL,
  CONSTRAINT matchrequest_pkey PRIMARY KEY (id),
  CONSTRAINT matchrequest_player_id_fkey FOREIGN KEY (player_id)
      REFERENCES public.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT matchrequest_match_id_fkey FOREIGN KEY (match_id)
      REFERENCES public.chessmatch (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.matchrequest
  OWNER TO chess;
