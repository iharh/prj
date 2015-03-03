# encoding: utf-8
require "logstash/outputs/base"
require "logstash/namespace"

class LogStash::Outputs::Sveout < LogStash::Outputs::Base
    begin
        require "ap"
        rescue LoadError
    end

    config_name "sveout"
    milestone 1
  
    default :codec, "line"

    config :silent, :validate => :boolean, :default => false

    public
    def register
        @joda_parser = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withDefaultYear(Time.new.year)
        #  @joda_parser = @joda_parser.withZone(org.joda.time.DateTimeZone.forID(@timezone))
        @joda_parser = @joda_parser.withOffsetParsed
        #@joda_parser = @joda_parser.withLocale(locale)

        @fx_start_millis = Hash.new(0)
        @fx_stop_millis = Hash.new(0)
        @fx_dur_millis = Hash.new(0)
        @fx_cnt = Hash.new(0)

        @idx_dur_millis = Hash.new(0)
        @idx_cnt = Hash.new(0)

        #warn is by default
        #@logger.info("My info") #, :field => field, :value => value, :exception => e)

        @codec.on_event do |event|
            unless @silent
                $stdout.write(event)
            end
        end
    end

    def receive(event)
        return unless output?(event)
        if event == LogStash::SHUTDOWN
            finished
            return
        end

        tags = event["tags"] 

        if tags.include?("fx_start")
            thread = event["thread"]
            timestamp = event["timestamp"] #@timestamp
            millis = @joda_parser.parseMillis(timestamp)

            #puts "t: #{timestamp.inspect()} class: #{timestamp.class}" #, " javaClass: ", timestamp.javaClass
            #puts "m: #{@fx_start_millis} class: #{millis.class}"

            event["fx_start_millis"] = millis
            @fx_start_millis[thread] = millis 

            @fx_stop_millis[thread] = 0
        elsif tags.include?("fx_stop")
            thread = event["thread"]
            fx_start_millis = @fx_start_millis[thread]
            if fx_start_millis > 0
                timestamp = event["timestamp"] #@timestamp
                millis = @joda_parser.parseMillis(timestamp)

                event["fx_stop_millis"] = millis
                @fx_stop_millis[thread] = millis

                dur = millis - fx_start_millis
                event["fx_dur_millis"] = dur
                @fx_dur_millis[thread] += dur

                @fx_start_millis[thread] = 0
                @fx_cnt[thread] += 1
            end
        elsif tags.include?("idx_start")
            thread = event["thread"]
            fx_stop_millis = @fx_stop_millis[thread]
            if fx_stop_millis > 0
                timestamp = event["timestamp"] #@timestamp
                millis = @joda_parser.parseMillis(timestamp)

                dur = millis - fx_stop_millis
                event["idx_dur_millis"] = dur
                @idx_dur_millis[thread] += dur

                @fx_stop_millis[thread] = 0
                @idx_cnt[thread] += 1
            end
        end

        @codec.encode(event) #codec_on_event triggered here

        if tags.include?("endfile")


            puts "FX duration:"
            fx_total_dur_millis = 0
            @fx_dur_millis.each do |k, v|
                fx_total_dur_millis += v
                puts "#{k}->#{v}"
            end

            puts "FX count:"
            fx_total_cnt = 0
            @fx_cnt.each do |k, v|
                fx_total_cnt += v
                puts "#{k}->#{v}"
            end

            puts "IDX duration:"
            idx_total_dur_millis = 0
            @idx_dur_millis.each do |k, v|
                idx_total_dur_millis += v
                puts "#{k}->#{v}"
            end

            puts "IDX count:"
            idx_total_cnt = 0
            @idx_cnt.each do |k, v|
                idx_total_cnt += v
                puts "#{k}->#{v}"
            end

            puts "FX total millis: #{fx_total_dur_millis} total count: #{fx_total_cnt}"
            puts "IDX total millis: #{idx_total_dur_millis} total count: #{idx_total_cnt}"
        end

        #@logger.debug? && @logger.debug("Date parsing done", :value => value, :timestamp => event["timestamp"])
    end
end # class LogStash::Outputs::Stdout
