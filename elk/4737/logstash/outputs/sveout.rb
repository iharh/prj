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

    public
    def register
        @joda_parser = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withDefaultYear(Time.new.year)
        #  @joda_parser = @joda_parser.withZone(org.joda.time.DateTimeZone.forID(@timezone))
        @joda_parser = @joda_parser.withOffsetParsed
        #@joda_parser = @joda_parser.withLocale(locale)

        @fx_start_millis = Hash.new(0)
        @fx_dur_millis = Hash.new(0)

        #warn is by default
        #@logger.info("My info") #, :field => field, :value => value, :exception => e)

        @codec.on_event do |event|
            $stdout.write(event)
            #$stdout.puts("hello")
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
            puts(thread)
            timestamp = event["timestamp"] #@timestamp
            millis = @joda_parser.parseMillis(timestamp)
            @fx_start_millis[thread] = millis 

            #puts "t: #{timestamp.inspect()} class: #{timestamp.class}" #, " javaClass: ", timestamp.javaClass
            #puts "m: #{@fx_start_millis} class: #{millis.class}"

            event["fx_start_millis"] = millis
        elsif tags.include?("fx_stop")
            thread = event["thread"]
            timestamp = event["timestamp"] #@timestamp
            millis = @joda_parser.parseMillis(timestamp)

            event["fx_stop_millis"] = millis
            dur = millis - @fx_start_millis[thread]
            event["fx_dur_millis"] = dur
            @fx_dur_millis[thread] += dur
        end

        @codec.encode(event) #codec_on_event triggered here

        if tags.include?("endfile")
            fx_total_dur_millis = 0
            @fx_dur_millis.each do |k, v|
                fx_total_dur_millis += v
                puts "#{k}->#{v}"
            end
            puts "FX total millis: #{fx_total_dur_millis}"
        end

        #@logger.debug? && @logger.debug("Date parsing done", :value => value, :timestamp => event["timestamp"])
    end
end # class LogStash::Outputs::Stdout
